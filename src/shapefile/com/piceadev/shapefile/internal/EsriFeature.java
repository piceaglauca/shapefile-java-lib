package com.piceadev.shapefile.internal;

public class EsriFeature {
    private int recordNumber;
    private int recordLength;
    private int recordShapeType;

    public void setRecordNumber (int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setRecordLength (int recordLength) {
        this.recordLength = recordLength;
    }

    public void setRecordShapeType (int recordShapeType) {
        this.recordShapeType = recordShapeType;
    }

    public int getRecordNumber () {
        return recordNumber;
    }

    public int getRecordLength () {
        return recordLength;
    }

    public int getRecordShapeType () {
        return recordShapeType;
    }
}
