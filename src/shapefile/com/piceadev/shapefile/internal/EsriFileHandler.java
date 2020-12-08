package com.piceadev.shapefile.internal;

import com.piceadev.shapefile.internal.LittleEndianRandomAccessFile;
import com.piceadev.shapefile.internal.EsriConstants;

public class EsriFileHandler extends LittleEndianRandomAccessFile {
    //private LittleEndianRandomAccessFile this;
    private String filename;
    private int shapeType;
    private int fileLength; // in 16-bit words, including 100 byte header

    public EsriFileHandler (String filename) {
        super(filename);
        //this = new LittleEndianRandomAccessFile (filename);
        this.filename = filename;
    }

    public int getFileCode () throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_CODE);

        return this.readInt();
    }

    public int getFileLength () throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_LENGTH);

        return this.readInt();
    }

    public int getVersion () throws IOException {
        this.seek(EsriConstants.HEADER_POS_VERSION);

        return this.readLEInt();
    }

    public int getShapeType () throws IOException {
        this.seek(EsriConstants.HEADER_POS_SHAPE_TYPE);

        return this.readLEInt ();
    }

    public double getXMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MIN);

        return this.readLEDouble ();
    }

    public double getXMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MAX);

        return this.readLEDouble ();
    }

    public double getYMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MIN);

        return this.readLEDouble ();
    }

    public double getYMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MAX);

        return this.readLEDouble ();
    }

    public double getZMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MIN);

        return this.readLEDouble ();
    }

    public double getZMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MAX);

        return this.readLEDouble ();
    }

    public double getMMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MIN);

        return this.readLEDouble ();
    }

    public double getMMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MAX);

        return this.readLEDouble ();
    }

    public void setFileCode (int fileCode) throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_CODE);
        this.writeInt(fileCode);
    }

    public void setFileLength (int fileLength) throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_LENGTH);
        this.writeInt(fileLength);
    }

    public void setVersion (int version) throws IOException {
        this.seek(EsriConstants.HEADER_POS_VERSION);
        this.writeLEInt(version);
    }

    public void setShapeType (int shapeType) throws IOException {
        this.seek(EsriConstants.HEADER_POS_SHAPE_TYPE);
        this.writeLEInt (shapeType);
    }

    public void setXMin (double xMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MIN);
        this.writeLEDouble (xMin);
    }

    public void setXMax (double xMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MAX);
        this.writeLEDouble (xMax);
    }

    public void setYMin (double yMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MIN);
        this.writeLEDouble (yMin);
    }

    public void setYMax (double yMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MAX);
        this.writeLEDouble (yMax);
    }

    public void setZMin (double zMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MIN);
        this.writeLEDouble (zMin);
    }

    public void setZMax (double zMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MAX);
        this.writeLEDouble (zMax);
    }

    public void setMMin (double mMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MIN);
        this.writeLEDouble (mMin);
    }

    public void setMMax (double mMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MAX);
        this.writeLEDouble (mMax);
    }

    public void addFeature (EsriFile file, int index, EsriFeature feature) {
        setFileLength = file.getFileLength() + feature.getRecordLength ();
    }
}
