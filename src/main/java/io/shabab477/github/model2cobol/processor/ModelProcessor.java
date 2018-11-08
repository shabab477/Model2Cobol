package io.shabab477.github.model2cobol.processor;

import io.shabab477.github.model2cobol.annotations.DataEntity;
import io.shabab477.github.model2cobol.annotations.DataMember;
import io.shabab477.github.model2cobol.annotations.EmbeddedData;
import io.shabab477.github.model2cobol.constants.CalculationStrategy;
import io.shabab477.github.model2cobol.exceptions.NotPrimitiveException;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shabab
 * @since 11/6/18
 */
class ModelProcessor {

    private static final String INFERRED_DATA_SIZE_MARKER = "<DATA-SIZE/>";

    protected String processModel(Object object, int level, int levelLimit) {
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
                "GRP"
        );

        cobol += "\n";

        int totalDataLength = 0;

        for(Field field : objectClass.getFields()) {

            if (field.isAnnotationPresent(DataMember.class)) {
                if (!field.getClass().isPrimitive()) {
                    throw  new NotPrimitiveException();
                }

                DataMember dataMember = field.getAnnotation(DataMember.class);
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
                        memberType
                );

                cobol += "\n";

            } else if (field.isAnnotationPresent(EmbeddedData.class)) {

            }
        }

        if (dataEntity.strategy() == CalculationStrategy.INFERRED) {
            cobol = cobol.replace(INFERRED_DATA_SIZE_MARKER, String.valueOf(totalDataLength));
        }

        return cobol;
    }

    private String createDataField(String dataLevel, String dataName, String dataLength, String dataType) {
        return dataLevel
                + "  "
                + dataName
                + IntStream.range(0, 17).mapToObj(i -> "\\s").collect(Collectors.joining(""))
                + dataLength
                + "/"
                + dataType;
    }

}
