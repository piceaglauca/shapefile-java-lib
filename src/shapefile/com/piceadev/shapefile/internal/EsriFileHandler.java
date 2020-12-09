package com.piceadev.shapefile.internal;

import java.util.logging.Logger;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.piceadev.shapefile.internal.LittleEndianRandomAccessFile;
import com.piceadev.shapefile.internal.EsriConstants;

/**
 * Parent class for reading and writing to/from ESRI .shp and .shx files.
 * This class contains methods common to SHP and SHX files, namely management
 * of the file header. DBF files have a different structure and require a 
 * different FileHandler (see DbfInputStream and DbfOutputStream).
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriFileHandler extends LittleEndianRandomAccessFile {

    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    protected String filename;
    protected int shapeType; // see EsriConstants for shape type identifiers
    protected int fileLength; // in 16-bit words, including 100 byte header

    /**
     * Constructor.
     *
     * @param filename  Filename of the shapefile or shape index file.
     */
    public EsriFileHandler (String filename) throws FileNotFoundException {
        super(filename);
        this.filename = filename;

        logger.fine ("EsriFileHandler using file " + filename);
    }

    /**
     * Read and return file code from the header. According to the ESRI shapefile 
     * specifications, this value will always be 9994.
     *
     * @return  fileCode
     */
    public int getFileCode () throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_CODE);
        int fileCode = this.readInt();

        logger.fine ("EsriFileHandler found file code " + fileCode);

        return fileCode;
    }

    /**
     * Read and return file length from the header. File length, in 16-byte 
     * words, including the 100-byte header.
     *
     * @return fileLength
     */
    public int getFileLength () throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_LENGTH);
        int fileLength = this.readInt();

        logger.fine ("EsriFileHandler found file length " + fileLength);

        return fileLength;
    }

    /**
     * Read and return the version number from the header. According to the ESRI
     * shapefile specifications, this value will always be 1000.
     *
     * @return  version
     */
    public int getVersion () throws IOException {
        this.seek(EsriConstants.HEADER_POS_VERSION);
        int version = this.readLEInt();

        logger.fine ("EsriFileHandler found version " + version);

        return version;
    }

    /**
     * Read and return the shape type from the header. The value is an integer 
     * representing a shape type. Each record in the file must be of the 
     * same type. See EsriConstants for shape type identifiers.
     *
     * @return  shapeType
     */
    public int getShapeType () throws IOException {
        this.seek(EsriConstants.HEADER_POS_SHAPE_TYPE);
        int shapeType = this.readLEInt ();

        logger.fine ("EsriFileHandler found shapeType " + shapeType);

        return shapeType;
    }

    /**
     * Read and return the Xmin component of the Bounding Box.
     *
     * @return  xMin
     */
    public double getXMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MIN);
        double xMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found xMin " + xMin);

        return xMin;
    }

    /**
     * Read and return the Xmax component of the Bounding Box.
     *
     * @return  xMax
     */
    public double getXMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MAX);
        double xMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found xMax " + xMax);

        return xMax;
    }

    /**
     * Read and return the Ymin component of the Bounding Box.
     *
     * @return  yMin
     */
    public double getYMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MIN);
        double yMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found yMin " + yMin);

        return yMin;
    }

    /**
     * Read and return the Ymax component of the Bounding Box.
     *
     * @return  yMax
     */
    public double getYMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MAX);
        double yMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found yMax " + yMax);

        return yMax;
    }

    /**
     * Read and return the Zmin component of the Bounding Box. This value will
     * be 0 unless it is a Z or M type (eg. EsriPointZ, EsriPolylineM, etc).
     *
     * @return  zMin
     */
    public double getZMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MIN);
        double zMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found zMin " + zMin);

        return zMin;
    }

    /**
     * Read and return the Zmax component of the Bounding Box. This value will
     * be 0 unless it is a Z or M type (eg. EsriPointZ, EsriPolylineM, etc).
     *
     * @return  zMax
     */
    public double getZMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MAX);
        double zMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found zMax " + zMax);

        return zMax;
    }

    /**
     * Read and return the Mmin component of the Bounding Box. This value will
     * be 0 unless it is a M type (eg. EsriPointM, EsriPolylineM, etc).
     *
     * @return  mMin
     */
    public double getMMin () throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MIN);
        double mMin = this.readLEDouble ();

        logger.fine ("EsriFileHandler found mMin " + mMin);

        return mMin;
    }

    /**
     * Read and return the Mmax component of the Bounding Box. This value will
     * be 0 unless it is a M type (eg. EsriPointM, EsriPolylineM, etc).
     *
     * @return  mMax
     */
    public double getMMax () throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MAX);
        double mMax = this.readLEDouble ();

        logger.fine ("EsriFileHandler found mMax " + mMax);

        return mMax;
    }

    /**
     * Set file code in file header. This method will only be needed if 
     * creating a new shapefile.
     *
     * @param   fileCode    New file code to write.
     */
    public void setFileCode (int fileCode) throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_CODE);
        this.writeInt(fileCode);

        logger.fine ("EsriFileHandler setting file code " + fileCode);
    }

    /**
     * Set file length in file header. This value will change if new features
     * are added.
     *
     * @param   fileLength  New file length to write.
     */
    public void setFileLength (int fileLength) throws IOException {
        this.seek(EsriConstants.HEADER_POS_FILE_LENGTH);
        this.writeInt(fileLength);

        logger.fine ("EsriFileHandler setting file length " + fileLength);
    }

    /**
     * Set version in file header. This method will only be needed if 
     * creating a new shapefile.
     *
     * @param   version    New version to write.
     */
    public void setVersion (int version) throws IOException {
        this.seek(EsriConstants.HEADER_POS_VERSION);
        this.writeLEInt(version);

        logger.fine ("EsriFileHandler setting version " + version);
    }

    /**
     * Set shape type in file header. This method will only be needed if 
     * creating a new shapefile.
     *
     * @param   shapeType    New shapeType to write.
     */
    public void setShapeType (int shapeType) throws IOException {
        this.seek(EsriConstants.HEADER_POS_SHAPE_TYPE);
        this.writeLEInt (shapeType);

        logger.fine ("EsriFileHandler setting shape type " + shapeType);
    }

    /**
     * Set Xmin component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   xMin    New xMin value to write.
     */
    public void setXMin (double xMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MIN);
        this.writeLEDouble (xMin);

        logger.fine ("EsriFileHandler setting xMin " + xMin);
    }

    /**
     * Set Xmax component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   xMax    New xMax value to write.
     */
    public void setXMax (double xMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_X_MAX);
        this.writeLEDouble (xMax);

        logger.fine ("EsriFileHandler setting xMax " + xMax);
    }

    /**
     * Set Ymin component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   yMin    New yMin value to write.
     */
    public void setYMin (double yMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MIN);
        this.writeLEDouble (yMin);

        logger.fine ("EsriFileHandler setting yMin " + yMin);
    }

    /**
     * Set Ymax component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   yMax    New yMax value to write.
     */
    public void setYMax (double yMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Y_MAX);
        this.writeLEDouble (yMax);

        logger.fine ("EsriFileHandler setting yMax " + yMax);
    }

    /**
     * Set Zmin component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   zMin    New zMin value to write.
     */
    public void setZMin (double zMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MIN);
        this.writeLEDouble (zMin);

        logger.fine ("EsriFileHandler setting zMin " + zMin);
    }

    /**
     * Set Zmax component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   zMax    New zMax value to write.
     */
    public void setZMax (double zMax) throws IOException {
        this.seek(EsriConstants.HEADER_POS_Z_MAX);
        this.writeLEDouble (zMax);

        logger.fine ("EsriFileHandler setting zMax " + zMax);
    }

    /**
     * Set Mmin component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   mMin    New mMin value to write.
     */
    public void setMMin (double mMin) throws IOException {
        this.seek(EsriConstants.HEADER_POS_M_MIN);
        this.writeLEDouble (mMin);

        logger.fine ("EsriFileHandler setting mMin " + mMin);
    }

    /**
     * Set Mmax component of Bounding Box. This method may be needed as new
     * features are added to the shapefile.
     *
     * @param   mMax    New mMax value to write.
     */
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
