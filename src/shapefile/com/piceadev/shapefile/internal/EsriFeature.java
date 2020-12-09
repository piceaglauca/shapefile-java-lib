package com.piceadev.shapefile.internal;

/**
 * Parent class for all EsriFeature. 
 * Current supported types are:
 *      - EsriPoint
 *      - EsriPolyline
 * Unsupported types are:
 *      - EsriPolygon
 *      - EsriMultiPoint
 *      - EsriPointZ
 *      - EsriPolylineZ
 *      - EsriPolygonZ
 *      - EsriMultiPointZ
 *      - EsriPointM
 *      - EsriPolylineM
 *      - EsriPolygonM
 *      - EsriMultiPointM
 *      - EsriMultiPatch
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriFeature {
    private int recordNumber;
    private int recordLength;
    private int recordShapeType;

    /**
     * Set the record number of the feature, part of the record header.
     *
     * @param   recordNumber    Number of the record.
     */
    public void setRecordNumber (int recordNumber) {
        this.recordNumber = recordNumber;
    }

    /**
     * Set the record length of the feature, part of the record header.
     *
     * @param   recordLength    Length of the record.
     */
    public void setRecordLength (int recordLength) {
        this.recordLength = recordLength;
    }

    /**
     * Set the shape type of the feature, part of the record header.
     *
     * @param   recordShapeType     Shape type of the record.
     */
    public void setRecordShapeType (int recordShapeType) {
        this.recordShapeType = recordShapeType;
    }

    /**
     * Get the record number of the feature.
     *
     * @return  recordNumber
     */
    public int getRecordNumber () {
        return recordNumber;
    }

    /**
     * Get the record length of the feature.
     *
     * @return  recordLength
     */
    public int getRecordLength () {
        return recordLength;
    }

    /**
     * Get the record shape type of the feature.
     *
     * @return  recordShapeType
     */
    public int getRecordShapeType () {
        return recordShapeType;
    }
}
