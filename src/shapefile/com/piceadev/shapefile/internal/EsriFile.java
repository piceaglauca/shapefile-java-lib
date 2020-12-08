package com.piceadev.shapefile.internal;

public class EsriFile {
    private EsriFileHandler fileHandler;

    private int fileCode;
    private int fileLength;
    private int version;
    private int shapeType;
    private double xMin, xMax;
    private double yMin, yMax;
    private double zMin, zMax;
    private double mMin, mMax;

    private void getHeaderInfo () {
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

    public int getFileLength () {
        return fileLength;
    }

    public int getShapeType () {
        return shapeType;
    }

    public double getXMin () {
        return xMin;
    }

    public double getXMax () {
        return xMax;
    }

    public double getYMin () {
        return yMin;
    }

    public double getYMax () {
        return yMax;
    }

    public double getZMin () {
        return zMin;
    }

    public double getZMax () {
        return zMax;
    }

    public double getMMin () {
        return mMin;
    }

    public double getMMax () {
        return mMax;
    }
}
