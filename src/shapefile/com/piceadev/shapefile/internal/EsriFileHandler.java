package com.piceadev.shapefile.internal;

import java.util.logging.Logger;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.piceadev.shapefile.internal.LittleEndianRandomAccessFile;
import com.piceadev.shapefile.internal.EsriConstants;

public class EsriFileHandler extends LittleEndianRandomAccessFile {

    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    protected String filename;
    protected int shapeType;
    protected int fileLength; // in 16-bit words, including 100 byte header

    public EsriFileHandler (String filename) throws FileNotFoundException {
        super(filename);
        this.filename = filename;

        logger.fine ("EsriFileHandler using file " + filename);
    }

    public int getFileCode () throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_CODE);
        int fileCode = this.readInt();

        logger.fine ("EsriFileHandler found file code " + fileCode);

        return fileCode;
    }

    public int getFileLength () throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_LENGTH);
        int fileLength = this.readInt();

        logger.fine ("EsriFileHandler found file length " + fileLength);

        return fileLength;
    }

    public int getVersion () throws IOException {
        this.seek(EsriConstants.HEADER_POS_VERSION);
        int version = this.readLEInt();

        logger.fine ("EsriFileHandler found version " + version);

        return version;
    }

    public int getShapeType () throws IOException {
        this.seek(EsriConstants.HEADER_POS_SHAPE_TYPE);
        int shapeType = this.readLEInt ();

        logger.fine ("EsriFileHandler found shapeType " + shapeType);

        return shapeType;
    }

    public double getXMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MIN);
        double xMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found xMin " + xMin);

        return xMin;
    }

    public double getXMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MAX);
        double xMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found xMax " + xMax);

        return xMax;
    }

    public double getYMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MIN);
        double yMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found yMin " + yMin);

        return yMin;
    }

    public double getYMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MAX);
        double yMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found yMax " + yMax);

        return yMax;
    }

    public double getZMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MIN);
        double zMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found zMin " + zMin);

        return zMin;
    }

    public double getZMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MAX);
        double zMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found zMax " + zMax);

        return zMax;
    }

    public double getMMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MIN);
        double mMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found mMin " + mMin);

        return mMin;
    }

    public double getMMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MAX);
        double mMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found mMax " + mMax);

        return mMax;
    }

    public void setFileCode (int fileCode) throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_CODE);
        this.writeInt(fileCode);

        logger.fine ("EsriFileHandler setting file code " + fileCode);
    }

    public void setFileLength (int fileLength) throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_LENGTH);
        this.writeInt(fileLength);

        logger.fine ("EsriFileHandler setting file length " + fileLength);
    }

    public void setVersion (int version) throws IOException {
        this.seek(EsriConstants.HEADER_POS_VERSION);
        this.writeLEInt(version);

        logger.fine ("EsriFileHandler setting version " + version);
    }

    public void setShapeType (int shapeType) throws IOException {
        this.seek(EsriConstants.HEADER_POS_SHAPE_TYPE);
        this.writeLEInt (shapeType);

        logger.fine ("EsriFileHandler setting shape type " + shapeType);
    }

    public void setXMin (double xMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MIN);
        this.writeLEDouble (xMin);

        logger.fine ("EsriFileHandler setting xMin " + xMin);
    }

    public void setXMax (double xMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MAX);
        this.writeLEDouble (xMax);

        logger.fine ("EsriFileHandler setting xMax " + xMax);
    }

    public void setYMin (double yMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MIN);
        this.writeLEDouble (yMin);

        logger.fine ("EsriFileHandler setting yMin " + yMin);
    }

    public void setYMax (double yMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MAX);
        this.writeLEDouble (yMax);

        logger.fine ("EsriFileHandler setting yMax " + yMax);
    }

    public void setZMin (double zMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MIN);
        this.writeLEDouble (zMin);

        logger.fine ("EsriFileHandler setting zMin " + zMin);
    }

    public void setZMax (double zMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MAX);
        this.writeLEDouble (zMax);

        logger.fine ("EsriFileHandler setting zMax " + zMax);
    }

    public void setMMin (double mMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MIN);
        this.writeLEDouble (mMin);

        logger.fine ("EsriFileHandler setting mMin " + mMin);
    }

    public void setMMax (double mMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MAX);
        this.writeLEDouble (mMax);

        logger.fine ("EsriFileHandler setting mMax " + mMax);
    }

    /*
    public void addFeature (EsriFile file, int index, EsriFeature feature) {
        setFileLength = file.getFileLength() + feature.getRecordLength ();
    }
    */
}
