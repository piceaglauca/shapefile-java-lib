package com.piceadev.shapefile.internal;

import java.io.IOException;

public class EsriFile {
    protected EsriFileHandler fileHandler;

    protected int fileCode;
    protected int fileLength;
    protected int version;
    protected int shapeType;
    protected double xMin, xMax;
    protected double yMin, yMax;
    protected double zMin, zMax;
    protected double mMin, mMax;

    protected void setFileHandler (EsriFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    protected void getHeaderInfo () throws IOException {
        this.fileCode = fileHandler.getFileCode ();
        this.fileLength = fileHandler.getFileLength ();
        this.version = fileHandler.getVersion ();
        this.shapeType = fileHandler.getShapeType ();
        this.xMin = fileHandler.getXMin ();
        this.xMax = fileHandler.getXMax ();
        this.yMin = fileHandler.getYMin ();
        this.yMax = fileHandler.getYMax ();
        this.zMin = fileHandler.getZMin ();
        this.zMax = fileHandler.getZMax ();
        this.mMin = fileHandler.getMMin ();
        this.mMax = fileHandler.getMMax ();
    }

    public int getFileLength () throws IOException {
        return fileLength;
    }

    public int getShapeType () throws IOException {
        return shapeType;
    }

    public double getXMin () throws IOException {
        return xMin;
    }

    public double getXMax () throws IOException {
        return xMax;
    }

    public double getYMin () throws IOException {
        return yMin;
    }

    public double getYMax () throws IOException {
        return yMax;
    }

    public double getZMin () throws IOException {
        return zMin;
    }

    public double getZMax () throws IOException {
        return zMax;
    }

    public double getMMin () throws IOException {
        return mMin;
    }

    public double getMMax () throws IOException {
        return mMax;
    }
}
