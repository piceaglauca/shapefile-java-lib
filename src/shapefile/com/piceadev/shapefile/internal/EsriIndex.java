package com.piceadev.shapefile.internal;

public class EsriIndex extends EsriFile {
    private int recordNumber;
    private int offset;
    private int contentLength;

    public void setRecordNumber (int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setOffset (int offset) {
        this.offset = offset;
    }
    
    public void setContentLength (int contentLength) {
        this.contentLength = contentLength;
    }
}
