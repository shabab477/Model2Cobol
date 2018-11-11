package io.shabab477.github.model2cobol.tests.generators;

import io.shabab477.github.model2cobol.generator.MemberValueGenerator;
import io.shabab477.github.model2cobol.processor.CobolLine;
import io.shabab477.github.model2cobol.processor.LineBuilder;

/**
 * @author shabab
 * @since 11/11/18
 */
public class ArrayGenerator implements MemberValueGenerator {

    @Override
    public boolean supports(Class<?> clazz) {
       return String[].class.equals(clazz);
    }

    @Override
    public void generateCode(LineBuilder lineBuilder, Object object) {
        String[] children = (String[]) object;

        int count = 1;

        lineBuilder.addLine(new CobolLine("", "", "BI", "2", "330"));

        for (String child : children) {
            lineBuilder.addLine(new CobolLine("4", "WS-LINE(" +  count + ")", "BI", "2", child));
            count++;
        }
    }
}
