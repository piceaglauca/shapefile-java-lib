package com.piceadev.shapefile.internal;

/**
 * Constants defined for ESRI Shapefiles.
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriConstants {

    /**
     * ESRI Shape types. All records in a shapefile must be of a consistent type.
     */
    public static final int NULL_SHAPE = 0;
    public static final int POINT = 1;
    public static final int POLYLINE = 3;
    public static final int POLYGON = 5;
    public static final int MULTIPOINT = 8;
    public static final int POINTZ = 11;
    public static final int POLYLINEZ = 13;
    public static final int POLYGONZ = 15;
    public static final int MULTIPOINTz = 18;
    public static final int POINTM = 21;
    public static final int POLYLINEM = 23;
    public static final int POLYGONM = 25;
    public static final int MULTIPOINTM = 28;
    public static final int MULTI_PATCH = 31;

    /**
     * DBF Types. Character identifiers and String tags.
     */
    public static final byte DBF_TYPE_BINARY = (byte) 'B';
    public static final String DBF_BINARY = "binary";
    public static final byte DBF_TYPE_CHARACTER = (byte) 'C';
    public static final String DBF_CHARACTER = "character";
    public static final byte DBF_TYPE_DATE = (byte) 'D';
    public static final String DBF_DATE = "date";
    public static final byte DBF_TYPE_NUMERIC = (byte) 'N';
    public static final String DBF_NUMERIC = "numeric";
    public static final byte DBF_TYPE_LOGICAL = (byte) 'L';
    public static final String DBF_LOGICAL = "boolean";
    public static final byte DBF_TYPE_MEMO = (byte) 'M';
    public static final String DBF_MEMO = "Memo";
    public static final byte DBF_TYPE_TIMESTAMP = (byte) '@';
    public static final String DBF_TIMESTAMP = "timestamp";
    public static final byte DBF_TYPE_LONG = (byte) 'I';
    public static final String DBF_LONG = "long";
    public static final byte DBF_TYPE_AUTOINCREMENT = (byte) '+';
    public static final String DBF_AUTOINCREMENT = "autoincrement";
    public static final byte DBF_TYPE_FLOAT = (byte) 'F';
    public static final String DBF_FLOAT = "float";
    public static final byte DBF_TYPE_DOUBLE = (byte) 'O';
    public static final String DBF_DOUBLE = "double";
    public static final byte DBF_TYPE_OLE = (byte) 'G';
    public static final String DBF_OLE = "OLE";
    
    /** 
     * Field byte locations in a SHP/SHX file header.
     */
    public static final short HEADER_POS_FILE_CODE = 0;
    public static final short HEADER_POS_FILE_LENGTH = 24;
    public static final short HEADER_POS_VERSION = 28;
    public static final short HEADER_POS_SHAPE_TYPE = 32;
    public static final short HEADER_POS_X_MIN = 36;
    public static final short HEADER_POS_Y_MIN = 44;
    public static final short HEADER_POS_X_MAX = 52;
    public static final short HEADER_POS_Y_MAX = 60;
    public static final short HEADER_POS_Z_MIN = 68;
    public static final short HEADER_POS_Z_MAX = 76;
    public static final short HEADER_POS_M_MIN = 84;
    public static final short HEADER_POS_M_MAX = 92;
    public static final short HEADER_END = 100;
}
