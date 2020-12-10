// **********************************************************************
// 
// <copyright>
// 
//  BBN Technologies
//  10 Moulton Street
//  Cambridge, MA 02138
//  (617) 873-8000
// 
//  Copyright (C) BBNT Solutions LLC. All rights reserved.
// 
// </copyright>
// **********************************************************************
// 
// $Source: /cvs/openmap/openmap/src/openmap/com/bbn/openmap/dataAccess/shape/DbfTableModel.java,v $
// $RCSfile: DbfTableModel.java,v $
// $Revision: 1.18 $
// $Date: 2009/02/05 18:47:15 $
// $Author: dietrick $
// 
// **********************************************************************

package com.piceadev.shapefile.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.piceadev.shapefile.internal.DbfInputStream;
import com.piceadev.shapefile.internal.DbfOutputStream;

/**
 * An implementation of TableModel that manages tabular data read from a dbf
 * file and enables the user to stored data store herein to be saved to a file
 * conforming to the DBF III file format specification.
 * 
 * To create a three one column model:
 * 
 * <pre>
 * DbfTableModel model = new DbfTableModel(1);
 * 
 * model.setDecimalCount(0, (byte) 0);
 * model.setLength(0, (byte) 10);
 * model.setColumnName(0, &quot;Column1&quot;);
 * model.setType(0, (byte) EsriConstants.DBF_TYPE_CHARACTER);
 * 
 * model.setDecimalCount(1, (byte) 0);
 * model.setLength(1, (byte) 10);
 * model.setColumnName(1, &quot;Column1&quot;);
 * model.setType(1, (byte) EsriConstants.DBF_TYPE_NUMERIC);
 * 
 * model.setDecimalCount(2, (byte) 0);
 * model.setLength(2, (byte) 10);
 * model.setColumnName(2, &quot;Column1&quot;);
 * model.setType(2, (byte) EsriConstants.DBF_TYPE_CHARACTER);
 * 
 * esriLayer.setModel(model);
 * </pre>
 * 
 * @author Doug Van Auken
 */
public class DbfTableModel extends AbstractTableModel
		implements TableModelListener, Iterable<List<Object>> {

    /*
     * Binary, Memo, OLE Fields and .DBT Files
     * 
     * Binary, memo, and OLE fields store data in .DBT files consisting of
     * blocks numbered sequentially (0, 1, 2, etc.). SET BLOCKSIZE determines
     * the size of each block. The first block in the .DBT file, block 0, is the
     * .DBT file header. Each binary, memo, or OLE field of each record in the
     * .DBF file contains the number of the block (in OEM code page values)
     * where the field's data actually begins. If a field contains no data, the
     * .DBF file contains blanks (0x20) rather than a number.
     * 
     * When data is changed in a field, the block numbers may also change and
     * the number in the .DBF may be changed to reflect the new location.
     */
    public static Logger logger = Logger.getLogger("com.piceadev.shapefile");

    /**
     * An array of bytes that contain the character lengths for each column
     */
    protected int[] _lengths = null;

    /**
     * An array of bytes that contain the number of decimal places for each
     * column
     */
    protected byte[] _decimalCounts = null;

    /** 
     * An array of bytes that contain the column types for each column 
     */
    protected byte[] _types = null;

    /** 
     * An array of bytes that contain the names for each column 
     */
    protected String[] _names = null;

    /** 
     * Class scope reference to a list of data formatted by row 
     */
    protected List<List<Object>> _records = null;

    /**
     * Class scope variable for the number of columns that exist in the model
     */
    protected int _columnCount = -1;

    protected boolean writable = false;

    protected boolean dirty = false;
    protected boolean exitOnClose = false;

    protected boolean DEBUG = false;

    // I18N mechanism
    //static I18n i18n = Environment.getI18n();

    protected DbfTableModel() {
        DEBUG = logger.isLoggable(Level.FINE);
    }

    /**
     * Creates a blank DbfTableModel
     * 
     * @param columnCount The number of columns this model will manage
     */
    public DbfTableModel(int columnCount) {
        this();
        _columnCount = columnCount;
        _records = new ArrayList<List<Object>>();
        _lengths = new int[columnCount];
        _decimalCounts = new byte[columnCount];
        _types = new byte[columnCount];
        _names = new String[columnCount];
    }

    /**
     * Creates a DbfTableModel based on an InputStream
     * 
     * @param is The dbf file
     */
    public DbfTableModel(DbfInputStream is) {
        this();
        _lengths = is.getLengths();
        _decimalCounts = is.getDecimalCounts();
        _names = is.getColumnNames();
        _types = is.getTypes();
        _records = is.getRecords();
        _columnCount = is.getColumnCount();
    }

    /**
     * Creates a DbfTableModel based on an InputStream
     * 
     * @param filename The dbf file
     */
    public DbfTableModel(String filename) throws IOException {
        this (new DbfInputStream (filename));
    }

    /**
     * Adds a row of data to the the model
     * 
     * @param columns A collection of columns that comprise the row of data
     * @exception An exception is thrown if the number of elements in the passed
     *            in collection does not match the number of columns in the
     *            model
     */
    public void addRecord(List<Object> columns) {
        if (columns.size() != _columnCount) {
            throw new RuntimeException("DbfTableModel: Mismatched Column Count");
        }
        _records.add(columns);
    }

    /**
     * Remove the record at the index.
     */
    public List<Object> remove(int index) {
        return _records.remove(index);
    }

    public void addBlankRecord() {
        addRecord(getBlankRecord());
    }

    public List<Object> getBlankRecord() {
        ArrayList<Object> record = new ArrayList<Object>();

        for (int i = 0; i < _columnCount; i++) {
            record.add(getEmptyDefaultForType(getType(i)));
        }
        return record;
    }

    public Object getEmptyDefaultForType(byte type) {
        // May need to be updated to provide real values.
        if (isNumericalType(type)) {
            return Double.valueOf(0);
        } else if (type == EsriConstants.DBF_TYPE_LOGICAL) {
            return Boolean.valueOf(false);
        } else {
            return "";
        }
    }

    public static boolean isNumericalType(byte type) {
        return type == EsriConstants.DBF_TYPE_NUMERIC 
                || type == EsriConstants.DBF_TYPE_LONG 
                || type == EsriConstants.DBF_TYPE_FLOAT
                || type == EsriConstants.DBF_TYPE_DOUBLE 
                || type == EsriConstants.DBF_TYPE_AUTOINCREMENT;
    }

    public static boolean isValidType(byte type) {
        return type == EsriConstants.DBF_TYPE_NUMERIC 
                || type == EsriConstants.DBF_TYPE_LONG 
                || type == EsriConstants.DBF_TYPE_FLOAT
                || type == EsriConstants.DBF_TYPE_DOUBLE 
                || type == EsriConstants.DBF_TYPE_BINARY 
                || type == EsriConstants.DBF_TYPE_CHARACTER
                || type == EsriConstants.DBF_TYPE_DATE 
                || type == EsriConstants.DBF_TYPE_LOGICAL 
                || type == EsriConstants.DBF_TYPE_MEMO
                || type == EsriConstants.DBF_TYPE_TIMESTAMP 
                || type == EsriConstants.DBF_TYPE_AUTOINCREMENT
                || type == EsriConstants.DBF_TYPE_OLE;
    }

    /**
     * Retrieves the record array list for the passed record number. In OpenMap,
     * Shape file records start their indexes at 0. This is the number attribute
     * stored in the EsriGraphic. Note: The Shape Specification and Shape files
     * contain indexes starting at 0. Make sure you don't get bit by an
     * off-by-one situation.
     * 
     * @param recordnumber The record number
     * @return An ArrayList for the given record number
     */
    public List<Object> getRecord(int recordnumber) {
        try {
            return _records.get(recordnumber);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            return null;
        }
    }

    /**
     * Get an iterator over the records.
     */
    public Iterator<List<Object>> getRecords() {
        return _records.iterator();
    }

	/**
	 * Iterable method for the records.
	 */
	public Iterator<List<Object>> iterator() {
		return _records.iterator();
	}

	/**
	 * Retrieves the column class for the passed in column index
	 * 
	 * @param c The column index
	 * @return The column class for the given column index
	 */
	public Class<? extends Object> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

    /**
     * Retrieves the number of columns that exist in the model
     * 
     * @return The number of columns that exist in the model
     */
    public int getColumnCount() {
        return _columnCount;
    }

    /**
     * Retrieves the number of decimal places for the passed in column index
     * 
     * @param column The column index
     * @return The number of decimal places for the given column index
     */
    public byte getDecimalCount(int column) {
        return _decimalCounts[column];
    }

    /**
     * Retrieves the column name for the passed in column index
     * 
     * @param column The column index
     * @return The column name for the given column index
     */
    public String getColumnName(int column) {
        return _names[column];
    }

    /**
     * Find the column index of the column with the given name.
     * 
     * @param columnName
     * @return If the columnName is valid, some number between 0-column count.
     *         Otherwise, -1 for non-valid names.
     */
    public int getColumnIndexForName(String columnName) {
        if (_names != null) {
            for (int i = 0; i < _names.length; i++) {
                if (_names[i].equalsIgnoreCase(columnName)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Retrieves the character length for the passed in column index
     * 
     * @param column The column index
     * @return The character length for the given column index
     */
    public int getLength(int column) {
        return _lengths[column];
    }

    /**
     * Retrieves the number of columns that exist in the model
     * 
     * @return The number column that exist in the model
     */
    public int getRowCount() {
        if (_records == null) {
            return 0;
        } else {
            return _records.size();
        }
    }

    /**
     * Retrieves the column type for the passed in column index
     * 
     * @param column The column index
     * @return The column type for the given column index
     */
    public byte getType(int column) {
        return _types[column];
    }

    /**
     * Retrieves a value for a specific column and row index
     * 
     * @return Object A value for a specific column and row index
     */
    public Object getValueAt(int row, int column) {
        List<Object> cells = _records.get(row);
        Object cell = cells.get(column);
        return cell;
    }

    /**
     * Sets the column name for the passed-in field index
     * 
     * @param column The column index
     * @param name The name to assign for the passed-in column index
     */
    public void setColumnName(int column, String name) {
        _names[column] = name;
    }

    /**
     * Sets the decimal count for the passed in field index
     * 
     * @param column The index to the column
     * @param decimalCount The number of decimals places to assign to the passed
     *        in column
     */
    public void setDecimalCount(int column, byte decimalCount) {
        _decimalCounts[column] = decimalCount;
    }

    /**
     * Set the character length for the passed-in field index
     * 
     * @param column The column index
     * @param length The character length to assign for the passed-in column
     *        index
     */
    public void setLength(int column, int length) {
        _lengths[column] = length;
    }

    /**
     * Sets the column type for the passed-in field index
     * 
     * @param column The column index
     * @param type The type of column to assign for the passed-in column index
     */
    public void setType(int column, byte type) {
        _types[column] = type;
    }

    public void setValueAt(Object object, int row, int column) {
        List<Object> columns = _records.get(row);
        columns.set(column, object);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return writable;
    }

    public void setWritable(boolean set) {
        writable = set;
    }

    public boolean getWritable() {
        return writable;
    }

    public void tableChanged(TableModelEvent e) {
        dirty = true;
        if (DEBUG)
            logger.fine("DbfTableModel sensing change");

        int row = e.getFirstRow();
        // Of course, the only thing we're listening to here is the
        // MetaDbfTableModel changes, and if we get a HEADER_ROW
        // change it's telling us to modify *OUR* headers, to commit
        // all the changes that were made to it. Otherwise, we should
        // just track the events and make them happen when we get
        // this.
        if (row == TableModelEvent.HEADER_ROW) {
            commitEvents((DbfTableModel) e.getSource());
        }
    }

    protected void commitEvents(DbfTableModel model) {

        if (DEBUG)
            logger.fine("Committing changes");

        Iterator<List<Object>> modelRecords = model.getRecords();
        int index = -1;

        while (modelRecords.hasNext()) {
            List<Object> modelRecord = modelRecords.next();

            String modelColumnName = (String) modelRecord.get(0);
            Byte modelType = (Byte) modelRecord.get(1);
            Integer modelLengthOfField = (Integer) modelRecord.get(2);
            Integer modelNumDecPlaces = (Integer) modelRecord.get(3);

            index++;
            if (index < _columnCount) {
                String columnName = _names[index];
                if (DEBUG)
                    logger.fine(columnName + ", " + modelColumnName);
                while (!columnName.equalsIgnoreCase(modelColumnName)) {
                    deleteColumn(index);
                    if (index >= _columnCount) {
                        addColumn(modelRecord);
                        break;
                    }
                    columnName = _names[index];
                }

                if (columnName.equalsIgnoreCase(modelColumnName)) {
                    _types[index] = modelType.byteValue();
                    _lengths[index] = modelLengthOfField.intValue();
                    _decimalCounts[index] = modelNumDecPlaces.byteValue();
                }

            } else {
                // Add Column
                if (DEBUG)
                    logger.fine("Add column " + modelColumnName);
                addColumn(modelRecord);
            }
        }

        while (++index < _columnCount) {
            if (DEBUG)
                logger.fine("Deleting extra column");
            deleteColumn(index);
        }

        if (DEBUG) {
            logger.fine("New Table:");
            for (int j = 0; j < _names.length; j++) {
                logger.fine("  " + _names[j]);
            }
        }
        fireTableStructureChanged();
        dirty = false;
    }

    /**
     * Delete a column, iterating through all the records and deleting that part
     * of each record.
     */
    protected void deleteColumn(int columnIndex) {
        Iterator<List<Object>> rows = getRecords();
        while (rows.hasNext()) {
            rows.next().remove(columnIndex);
        }

        _columnCount -= 1;

        _lengths = remove(_lengths, columnIndex);
        _decimalCounts = remove(_decimalCounts, columnIndex);
        _types = remove(_types, columnIndex);
        _names = remove(_names, columnIndex);
    }

    /**
     */
    protected int[] remove(int[] current, int index) {

        int[] newBytes = new int[current.length - 1];
        System.arraycopy(current, 0, newBytes, 0, index);
        System.arraycopy(current, index + 1, newBytes, index, current.length - index - 1);
        return newBytes;
    }

    /**
     */
    protected byte[] remove(byte[] current, int index) {

        byte[] newBytes = new byte[current.length - 1];
        System.arraycopy(current, 0, newBytes, 0, index);
        System.arraycopy(current, index + 1, newBytes, index, current.length - index - 1);
        return newBytes;
    }

    /**
     */
    protected String[] remove(String[] current, int index) {

        String[] newStrings = new String[current.length - 1];
        System.arraycopy(current, 0, newStrings, 0, index);
        System.arraycopy(current, index + 1, newStrings, index, current.length - index - 1);
        return newStrings;
    }

    /**
     * The types in the ArrayList are set - String, Byte, Integer, Integer - to
     * match the format of the header.
     */
    protected void addColumn(List<Object> recordColumn) {

        Iterator<List<Object>> rows = getRecords();
        while (rows.hasNext()) {
            rows.next().add("");
        }

        _columnCount++;

        _names = add(_names, ((String) recordColumn.get(0)));
        _types = add(_types, ((Byte) recordColumn.get(1)).byteValue());
        _lengths = add(_lengths, ((Integer) recordColumn.get(2)).byteValue());
        _decimalCounts = add(_decimalCounts, ((Integer) recordColumn.get(3)).byteValue());
    }

    /**
     */
    protected int[] add(int[] current, byte nb) {
        int[] newBytes = new int[current.length + 1];
        System.arraycopy(current, 0, newBytes, 0, current.length);
        newBytes[current.length] = nb;
        return newBytes;
    }

    /**
     */
    protected byte[] add(byte[] current, byte nb) {
        byte[] newBytes = new byte[current.length + 1];
        System.arraycopy(current, 0, newBytes, 0, current.length);
        newBytes[current.length] = nb;
        return newBytes;
    }

    protected String[] add(String[] current, String string) {
        String[] newStrings = new String[current.length + 1];
        System.arraycopy(current, 0, newStrings, 0, current.length);
        newStrings[current.length] = string;
        return newStrings;
    }

    public void cleanupChanges() {
        //if (DEBUG)
        //    Debug.output("DbfTableModel cleaning up changes.");
        dirty = false;
    }

    /**
     * Create another DbfTableModel with the same structure as this one (number
     * of columns, column names, lengths and decimal counts).
     */
    public DbfTableModel headerClone() {
        int size = getColumnCount();
        DbfTableModel dtm = new DbfTableModel(size);
        for (int i = 0; i < size; i++) {
            dtm.setColumnName(i, this.getColumnName(i));
            dtm.setDecimalCount(i, this.getDecimalCount(i));
            dtm.setLength(i, this.getLength(i));
            dtm.setType(i, this.getType(i));
        }
        return dtm;
    }

    public static DbfTableModel read(String filename)
            throws Exception {
        DbfTableModel model = new DbfTableModel(filename);
        return model;
    }

    public static String write(DbfTableModel model, String location)
            throws FileNotFoundException, IOException {
        if (location == null) {

            throw new IOException ("File path not specified.");
        }
        if (location != null) {
            DbfOutputStream dos = new DbfOutputStream(location);
            dos.writeModel(model);
            dos.close();
        }

        return location;
    }

    /**
     * Takes the object and converts it to a String for output.
     * 
     * @param obj
     * @param type
     * @param df
     * @param columnLength the length of the column entries, so whitespace can
     *        be added if needed.
     * @return a string value representing the object. If the object is null, a
     *         whitespace string will be returned.
     */
    public static String getStringForType(Object obj, byte type, DecimalFormat df, int columnLength) {
        String ret = "";

        if (obj != null) {

            if (isNumericalType(type)) {
                try {
                    ret = df.format(((Double) obj).doubleValue());
                } catch (Exception e) {
                    ret = "";
                }

            } else if (obj instanceof String) {
                ret = (String) obj;
            }
        }

        if (ret.length() != columnLength) {
            ret = appendWhitespaceOrTrim(ret, columnLength);
        }

        return ret;
    }

    /**
     * A method that looks at the length of String s and returns a copy of it
     * with whitespace appended to the end to allow it to have the provided
     * length.
     * 
     * @param s
     * @param length
     * @return String with whitespace added if needed.
     */
    public static String appendWhitespaceOrTrim(String s, int length) {
        if (s == null) {
            s = new String("");
        }

        int retLength = s.length();
        if (retLength < length) {
            StringBuffer sb = new StringBuffer(s);
            int numWhites = length - retLength;
            for (int i = 0; i < numWhites; i++) {
                sb.append(" ");
            }
            s = sb.toString();
        } else if (retLength > length) {
            s = s.substring(0, length);
        }

        return s;
    }

    public static Object getObjectForType(String cellContents, int type, DecimalFormat df, int columnLength)
            throws java.text.ParseException {
        Object ret = cellContents;
        if (isNumericalType((byte) type)) {
            if (cellContents.length() > 0) {
                try {
                    ret = Double.parseDouble(cellContents);
                } catch (NumberFormatException nfe) {
                    // Shouldn't get here, but thought it might help. DFD
                    ret = (double) df.parse(cellContents).doubleValue();
                }
            } else {
                // If we come across a numerical cell that doesn't contain data,
                // we should create an empty whitespace string in that cell
                // instead of setting it to ZERO. ZERO has a very different
                // meaning.

                ret = appendWhitespaceOrTrim(null, columnLength);
            }
        } else if (type == EsriConstants.DBF_TYPE_BINARY || type == EsriConstants.DBF_TYPE_MEMO || type == EsriConstants.DBF_TYPE_OLE) {
            if (cellContents.length() < 10) {
                cellContents = cellContents.trim();
                // prepending whitespace.
                StringBuffer bu = new StringBuffer();
                int numSpaces = 10 - cellContents.length();
                for (int i = 0; i < numSpaces; i++) {
                    bu.append(" ");
                }
                bu.append(cellContents);
                ret = bu.toString();
            }
        } else if (type == EsriConstants.DBF_TYPE_TIMESTAMP) {
            // uhhhhhh....
        }

        return ret;
    }

    public boolean isExitOnClose() {
        return exitOnClose;
    }

    public void setExitOnClose(boolean exitOnClose) {
        this.exitOnClose = exitOnClose;
    }

    public static void test() {
        DbfTableModel dtm = new DbfTableModel(2);
        dtm.setColumnName(0, "NAME");
        dtm.setColumnName(1, "VALUE");

        dtm.setDecimalCount(0, (byte) 0);
        dtm.setDecimalCount(1, (byte) 3);

        dtm.setLength(0, 20);
        dtm.setLength(1, 10);

        dtm.setType(0, EsriConstants.DBF_TYPE_CHARACTER);
        dtm.setType(1, EsriConstants.DBF_TYPE_DOUBLE);

        ArrayList<Object> record = new ArrayList<Object>();
        record.add("ok");
        record.add((double) 345.3);
        dtm.addRecord(record);

        record = new ArrayList<Object>();
        record.add("null");
        record.add(null);
        dtm.addRecord(record);

        record = new ArrayList<Object>();
        record.add("blank");
        record.add("");
        dtm.addRecord(record);

        record = new ArrayList<Object>();
        record.add("zero");
        record.add((double) 0);
        dtm.addRecord(record);

        try {
            write(dtm, "./test.dbf");

            //dtm = read(new File("./test.dbf").toURI().toURL());
            dtm = read("./test.dbf");

            //dtm.showGUI("test.dbf", MODIFY_ROW_MASK | MODIFY_COLUMN_MASK | SAVE_MASK);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public boolean matches(DbfTableModel dbf) {
        boolean columnsMatch = false;

        if (dbf != null) {
            int columnCount = getColumnCount();
            columnsMatch = dbf.getColumnCount() == columnCount;

            int i = 0;
            while (columnsMatch && i < columnCount) {
                columnsMatch = columnsMatch && dbf.getColumnName(i).equals(getColumnName(i));
                i++;
            }
        }
        return columnsMatch;
    }

    public void append(DbfTableModel dbf) {
        if (matches(dbf)) {
            Iterator<List<Object>> it = dbf.getRecords();
            while (it.hasNext()) {
                addRecord(it.next());
            }
        }
    }
}
