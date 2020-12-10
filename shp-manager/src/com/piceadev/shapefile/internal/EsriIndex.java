package com.piceadev.shapefile.internal;

/**
 * Class to represent a record in the shape index file (SHX).
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriIndex {
    private int recordNumber;
    private int offset;
    private int contentLength;

    public int getRecordNumber () {
        return recordNumber;
    }

    public int getOffset () {
        return offset;
    }

    public int getContentLength () {
        return contentLength;
    }

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
