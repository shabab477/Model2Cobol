package io.shabab477.github.model2cobol.processor;

import io.shabab477.github.model2cobol.annotations.DataEntity;
import io.shabab477.github.model2cobol.annotations.DataMember;
import io.shabab477.github.model2cobol.annotations.Embedded;
import io.shabab477.github.model2cobol.constants.CalculationStrategy;
import io.shabab477.github.model2cobol.exceptions.NotPrimitiveException;
import io.shabab477.github.model2cobol.generator.MemberValueGenerator;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shabab
 * @since 11/6/18
 */
class ModelProcessor {

    private static final String INFERRED_DATA_SIZE_MARKER = "<DATA-SIZE/>";

    static String processModel(Object object, int level, int levelLimit, AtomicInteger totalDataLength) {
        String cobol = "";
        if (level > levelLimit) {
            return cobol;
        }

        if (object.getClass() == null) {
            return "";
        }

        Class<?> objectClass = object.getClass();
        DataEntity dataEntity = objectClass.getAnnotation(DataEntity.class);
        String rootName = dataEntity.dataName();
        String dataLength = Integer.toString(dataEntity.length());
        String dataLevel = Integer.toString(dataEntity.level());

        cobol += IntStream.range(0, (level - 1) * 2).mapToObj(i -> " ").collect(Collectors.joining(""));
        cobol += Util.createDataField(
                dataLevel,
                rootName,
                dataEntity.strategy() == CalculationStrategy.INFERRED ? INFERRED_DATA_SIZE_MARKER : dataLength,
                "GRP",
                null
        );

        cobol += "\n";
        int totalScopedLength = 0;

        for(Field field : objectClass.getDeclaredFields()) {


            if(field.isAnnotationPresent(DataMember.class)
                    && (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType()))
            ){


                DataMember dataMember = field.getAnnotation(DataMember.class);
                //TODO: Handle default behavior
                if (Void.class.isAssignableFrom(dataMember.parseStrategy())) {

                    Collection<Object> collection = new ArrayList<>();

                    if (field.getType().isArray()) {

                        int length = 0;
                        boolean isAccessible = field.isAccessible();
                        field.setAccessible(true);

                        try {
                            length = Array.getLength(field.get(object));

                            for (int c = 0; c < length; c++) {
                                collection.add(Array.get(field.get(object), c));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        field.setAccessible(isAccessible);
                    }

                    if (Collection.class.isAssignableFrom(field.getType())) {
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);

                        try {
                            collection = (Collection) field.get(field);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        if (!accessible) {
                            field.setAccessible(false);
                        }
                    }

                    cobol += IntStream.range(0, level * 2).mapToObj(i -> " ").collect(Collectors.joining(""));
                    cobol += dataMember.level();
                    cobol += IntStream.range(0, level * 2).mapToObj(i -> " ").collect(Collectors.joining(""));
                    cobol += dataMember.dataName() + "(1) ";
                    cobol += "OCCURS " + collection.size() + " times\n";

                    for (Object item : collection) {
                        String value = String.valueOf(item);
                        cobol += Util.createDataField(
                                String.valueOf(dataMember.level()),
                                dataMember.dataName(),
                                String.valueOf(dataMember.length()),
                                dataMember.type(),
                                value
                        );

                        cobol += "\n";
                    }
                }

            }

            if (field.isAnnotationPresent(DataMember.class)) {
                if (!field.getClass().isPrimitive() && String.class.isAssignableFrom(field.getClass())) {
                    throw  new NotPrimitiveException();
                }

                DataMember dataMember = field.getAnnotation(DataMember.class);

                Class parseStrategyClass = dataMember.parseStrategy();

                if (MemberValueGenerator.class.isAssignableFrom(parseStrategyClass)) {
                    Constructor<?> constructor = null;
                    boolean accessible = false;
                    try {
                        constructor = parseStrategyClass.getConstructor();
                        accessible = constructor.isAccessible();
                        constructor.setAccessible(true);
                        MemberValueGenerator instance = (MemberValueGenerator)constructor.newInstance();

                        boolean isFieldAccessible = field.isAccessible();
                        field.setAccessible(true);

                        if (instance.supports(field.get(object).getClass())) {
                            LineBuilder lineBuilder = new LineBuilder();

                            instance.generateCode(lineBuilder, field.get(object));
                            AtomicInteger tempReference = new AtomicInteger();
                            cobol += lineBuilder.getUnifiedLines(level, tempReference);
                            totalScopedLength += tempReference.get();
                        }

                        if (isFieldAccessible) {
                            field.setAccessible(false);
                        }

                    } catch (InstantiationException e) {
                        throw new RuntimeException("Must have default public constructor");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("No publicly available default constructor");
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException("Could find public constructor");
                    } finally {
                        if (!accessible && constructor != null) {
                            constructor.setAccessible(accessible);
                        }
                    }

                } else if (!field.getType().isArray() && !Collection.class.isAssignableFrom(field.getType())){

                    int memberLevel = dataMember.level();
                    String memberName = dataMember.dataName();
                    int memberLength = dataMember.length();
                    String memberType = dataMember.type();

                    totalScopedLength += memberLength;

                    cobol += IntStream.range(0, level * 2).mapToObj(i -> " ").collect(Collectors.joining(""));
                    cobol += Util.createDataField(
                            String.valueOf(memberLevel),
                            memberName,
                            String.valueOf(memberLength),
                            memberType,
                            Util.getFieldValue(field, object)
                    );

                    cobol += "\n";
                }

            } else if (field.isAnnotationPresent(Embedded.class)) {
                boolean accessible = field.isAccessible();

                field.setAccessible(true);
                try {
                    cobol += processModel(field.get(object), level + 1, levelLimit, totalDataLength);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (!accessible) {
                    field.setAccessible(false);
                }

            }
        }

        totalDataLength.addAndGet(totalScopedLength);

        if (dataEntity.strategy() == CalculationStrategy.INFERRED) {
            cobol = cobol.replace(INFERRED_DATA_SIZE_MARKER, String.valueOf(totalDataLength.get()));
        }

        return cobol;
    }


}
