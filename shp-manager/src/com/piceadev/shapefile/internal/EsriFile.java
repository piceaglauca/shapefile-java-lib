package com.piceadev.shapefile.internal;

import java.io.IOException;

/**
 * Parent class for managing ESRI SHP and SHX data. This class contains methods
 * common to SHP and SHX files, namely management of the file header. DBF files
 * have a different structure and require a different class (see DbfTableModel).
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
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

    /**
     * No constructor required.
     */

    /**
     * Initialize fileHandler with subclass fileHandler. Type will be
     * ShpFileHandler or ShxFileHandler.
     *
     * @param   EsriFileHandler     Subclass fileHandler.
     */
    protected void setFileHandler (EsriFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * Read header info and populate member fields.
     */
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

    /**
     * Get length of file (SHP or SHX) in 16-byte words, including the 100-byte
     * header.
     *
     * @return  fileLength in 16-byte words
     */
    public int getFileLength () throws IOException {
        return fileLength;
    }

    
    /**
     * Return the shape type from the header. The value is an integer 
     * representing a shape type. Each record in the file must be of the 
     * same type. See EsriConstants for shape type identifiers.
     *
     * @return  shapeType
     */
    public int getShapeType () throws IOException {
        return shapeType;
    }

    /**
     * Return the Xmin component of the Bounding Box.
     *
     * @return  xMin
     */
    public double getXMin () throws IOException {
        return xMin;
    }

    /**
     * Return the Xmin component of the Bounding Box.
     *
     * @return  xMin
     */
    public double getXMax () throws IOException {
        return xMax;
    }

    /**
     * Return the Ymin component of the Bounding Box.
     *
     * @return  yMin
     */
    public double getYMin () throws IOException {
        return yMin;
    }

    /**
     * Return the Ymin component of the Bounding Box.
     *
     * @return  yMin
     */
    public double getYMax () throws IOException {
        return yMax;
    }

    /**
     * Return the Zmin component of the Bounding Box. This value will
     * be 0 unless it is a Z or M type (eg. EsriPointZ, EsriPolylineM, etc).
     *
     * @return  zMin
     */
    public double getZMin () throws IOException {
        return zMin;
    }

    /**
     * Return the Zmax component of the Bounding Box. This value will
     * be 0 unless it is a Z or M type (eg. EsriPointZ, EsriPolylineM, etc).
     *
     * @return  zMax
     */
    public double getZMax () throws IOException {
        return zMax;
    }

    /**
     * Return the Mmin component of the Bounding Box. This value will
     * be 0 unless it is a M type (eg. EsriPointM, EsriPolylineM, etc).
     *
     * @return  mMin
     */
    public double getMMin () throws IOException {
        return mMin;
    }

    /**
     * Return the Mmax component of the Bounding Box. This value will
     * be 0 unless it is a M type (eg. EsriPointM, EsriPolylineM, etc).
     *
     * @return  mMax
     */
    public double getMMax () throws IOException {
        return mMax;
    }
}
