package io.shabab477.github.model2cobol.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author shabab
 * @since 11/11/18
 */
public class LineBuilder {

    private List<CobolLine> cobolLines = new ArrayList<>();

    public void addLine(CobolLine cobolLine) {
        cobolLines.add(cobolLine);
    }

    protected String getUnifiedLines(int level, AtomicInteger totalLength) {
        String lines = "";
        for (CobolLine cobolLine : cobolLines) {
            lines += IntStream.range(0, level * 2).mapToObj(i -> " ").collect(Collectors.joining(""));

            lines += Util.createDataField(
                cobolLine.getDataLevel(),
                cobolLine.getDataName(),
                cobolLine.getLength(),
                cobolLine.getDataType(),
                cobolLine.getDataValue()
            );

            totalLength.addAndGet(Integer.parseInt(cobolLine.getLength()));

            lines += "\n";
        }

        return lines;
    }
}
