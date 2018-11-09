package io.shabab477.github.model2cobol.processor;

public class CobolLine {

    private String dataLevel;
    private String dataName;
    private String dataType;
    private String length;
    private String dataValue;

    public CobolLine() {
    }

    public CobolLine(String dataLevel, String dataName, String dataType, String length, String dataValue) {
        this.dataLevel = dataLevel;
        this.dataName = dataName;
        this.dataType = dataType;
        this.length = length;
        this.dataValue = dataValue;
    }

    public String getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(String dataLevel) {
        this.dataLevel = dataLevel;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
