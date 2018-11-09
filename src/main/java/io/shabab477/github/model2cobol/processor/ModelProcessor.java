package io.shabab477.github.model2cobol.processor;

import io.shabab477.github.model2cobol.annotations.DataEntity;
import io.shabab477.github.model2cobol.annotations.DataMember;
import io.shabab477.github.model2cobol.constants.CalculationStrategy;
import io.shabab477.github.model2cobol.exceptions.NotPrimitiveException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shabab
 * @since 11/6/18
 */
class ModelProcessor {

    private static final String INFERRED_DATA_SIZE_MARKER = "<DATA-SIZE/>";

    static String processModel(Object object, int level, int levelLimit) {
        String cobol = "";
        if (level > levelLimit) {
            return cobol;
        }

        Class<?> objectClass = object.getClass();
        DataEntity dataEntity = objectClass.getAnnotation(DataEntity.class);
        String rootName = dataEntity.dataName();
        String dataLength = Integer.toString(dataEntity.length());
        String dataLevel = Integer.toString(dataEntity.level());

        cobol += IntStream.range(0, (level - 1) * 2).mapToObj(i -> "\\s").collect(Collectors.joining(""));
        cobol += createDataField(
                dataLevel,
                rootName,
                dataEntity.strategy() == CalculationStrategy.INFERRED ? INFERRED_DATA_SIZE_MARKER : dataLength,
                "GRP",
                null
        );

        cobol += "\n";

        int totalDataLength = 0;

        for(Field field : objectClass.getFields()) {
            if(field.isAnnotationPresent(DataMember.class)
                    && (field.getClass().isArray() || Collection.class.isAssignableFrom(field.getType()))
            ){
                //TODO: Handle parse strategy

                //TODO: Handle default behavior
                DataMember dataMember = field.getAnnotation(DataMember.class);
                Collection<Object> collection = new ArrayList<>();

                if (field.getClass().isArray()) {
                    int length = Array.getLength(field);
                    for (int c = 0; c < length; c++) {
                        collection.add(Array.get(field, c));
                    }
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

                cobol += dataMember.level();
                cobol += IntStream.range(0, level * 2).mapToObj(i -> "\\s").collect(Collectors.joining(""));
                cobol += "OCCURS " + dataMember.length() + " times\n";
                //Todo: Handle parse strategy

                for (Object item : collection) {
                    String value = String.valueOf(item);
                    cobol += createDataField(
                        String.valueOf(dataMember.level()),
                        dataMember.dataName(),
                        String.valueOf(dataMember.length()),
                        dataMember.type(),
                        value
                    );
                }

            } else if (field.isAnnotationPresent(DataMember.class)) {
                if (!field.getClass().isPrimitive()) {
                    throw  new NotPrimitiveException();
                }

                DataMember dataMember = field.getAnnotation(DataMember.class);
                //TODO: Handle parse strategies
                int memberLevel = dataMember.level();
                String memberName = dataMember.dataName();
                int memberLength = dataMember.length();
                String memberType = dataMember.type();

                totalDataLength += memberLength;
                cobol += IntStream.range(0, level * 2).mapToObj(i -> "\\s").collect(Collectors.joining(""));
                cobol += createDataField(
                        String.valueOf(memberLevel),
                        memberName,
                        String.valueOf(memberLength),
                        memberType,
                        getFieldValue(field, object)
                );

                cobol += "\n";

            } else if (field.isAnnotationPresent(DataEntity.class)) {
                boolean accessible = field.isAccessible();

                field.setAccessible(true);
                try {
                    cobol += processModel(field.get(object), level + 1, levelLimit);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (!accessible) {
                    field.setAccessible(false);
                }
            }
        }

        if (dataEntity.strategy() == CalculationStrategy.INFERRED) {
            cobol = cobol.replace(INFERRED_DATA_SIZE_MARKER, String.valueOf(totalDataLength));
        }

        return cobol;
    }

    @NotNull
    private static String createDataField(String dataLevel, String dataName, String dataLength, String dataType, String value) {
        String line =  dataLevel
                        + "  "
                        + dataName
                        + IntStream.range(0, 17).mapToObj(i -> "\\s").collect(Collectors.joining(""))
                        + dataLength
                        + "/"
                        + dataType;
        if(value != null && value.length() != 0) {
            line += "  " + value;
        }

        return line;
    }

    private static String getFieldValue(Field field, Object object) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        String value = "";
        try {
            value = String.valueOf(field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(!accessible) {
            field.setAccessible(false);
        }

        return value;
    }
}
