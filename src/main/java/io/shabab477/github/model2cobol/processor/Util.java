package io.shabab477.github.model2cobol.processor;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shabab
 * @since 11/11/18
 */
public class Util {

    protected static String createDataField(String dataLevel, String dataName, String dataLength, String dataType, String value) {
        String line =  (dataLevel.length() == 0? " " : dataLevel)
                + "  "
                + dataName
                + IntStream.range(0, 29 - dataName.length()).mapToObj(i -> " ").collect(Collectors.joining(""))
                + dataLength
                + "/"
                + dataType;

        if(value != null && value.length() != 0) {
            line += "  " + value;
        }

        return line;
    }

    protected static String getFieldValue(Field field, Object object) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        String value = "";
        try {
            value = String.valueOf(field.get(object) == null? "" : field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(!accessible) {
            field.setAccessible(false);
        }

        return value;
    }
}
