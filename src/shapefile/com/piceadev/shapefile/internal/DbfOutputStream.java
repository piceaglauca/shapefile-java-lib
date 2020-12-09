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
// $Source: /cvs/distapps/openmap/src/openmap/com/bbn/openmap/dataAccess/shape/output/DbfOutputStream.java,v $
// $RCSfile: DbfOutputStream.java,v $
// $Revision: 1.18 $
// $Date: 2009/02/05 18:46:11 $
// $Author: dietrick $
// 
// **********************************************************************

package com.piceadev.shapefile.internal;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.piceadev.shapefile.internal.DbfTableModel;
import com.piceadev.shapefile.internal.LittleEndianRandomAccessFile;

/**
 * Writes date in a DbfTableModel to a file, conforming to the DBF III file
 * format specification
 * 
 * @author Doug Van Auken
 */
public class DbfOutputStream {
    /**
     * An outputstream that writes primitive data types in little endian or big
     * endian
     */
    private LittleEndianRandomAccessFile _fileHandler;

    /**
     * Creates a DbfOutputStream
    public DbfOutputStream(OutputStream os) {
        BufferedOutputStream bos = new BufferedOutputStream(os);
        _fileHandler = new LittleEndianOutputStream(bos);
    }
     */
    public DbfOutputStream(String filename) throws FileNotFoundException {
        _fileHandler = new LittleEndianRandomAccessFile(filename);
    }

    /**
     * Writes the model out on the stream. The stream is closed automatically
     * after the write.
     * 
     * @param model the tablemodel to write
     */
    public void writeModel(DbfTableModel model) throws IOException {
        int rowCount = model.getRowCount();
        short headerLength = calcHeaderLength(model);
        short recordLength = calcRecordLength(model);

        writeHeader(rowCount, headerLength, recordLength);
        writeFieldDescriptors(model);
        writeRecords(model);
    }

    /**
     * Calculates the length of the record by aggregating the length of each
     * field
     * 
     * @param model The DbfTableModel for which to calculate the record length
     * @return The length of a record
     */
    public short calcRecordLength(DbfTableModel model) {
        int length = 0;
        int columnCount = model.getColumnCount();
        for (int i = 0; i <= columnCount - 1; i++) {
            length += model.getLength(i);
        }
        length += 1;
        Integer integer = Integer.valueOf(length);
        return integer.shortValue();
    }

    /**
     * Calculates the length of the header in terms of bytes
     * 
     * @param model The DbfTableModel for which to calculate header length
     * @return The header length
     */
    public short calcHeaderLength(DbfTableModel model) {
        int length = 0;
        length += model.getColumnCount() * 32; // 32 bytest for each
        // record
        length += 32; // 32 bytes for the record
        length += 1; // 1 byte for header terminator
        Integer integer = Integer.valueOf (length);
        return integer.shortValue();
    }

    /**
     * Writes the header to the class scope LittleEndianOutputStream
     * 
     * @param rowCount The number of records
     * @param headerLength The length, in terms of bytes, of the header section
     * @param recordLength The length, in terms of bytes, of each records
     */
    private void writeHeader(int rowCount, short headerLength,
                             short recordLength) throws IOException {

        _fileHandler.writeByte(3); // byte 0
        _fileHandler.writeByte(96); // Byte 1 - Year
        _fileHandler.writeByte(4); // Byte 2 - Month
        _fileHandler.writeByte(30); // Byte 3 - Day
        _fileHandler.writeLEInt(rowCount); // Byte 4 Number of records in the
        // table
        _fileHandler.writeLEShort(headerLength); // byte 8 Number of bytes in
        // the header
        _fileHandler.writeLEShort(recordLength); // byte 10 Number of bytes
        // in the record
        _fileHandler.writeByte(0); // Byte 12
        _fileHandler.writeByte(0); // Byte 13
        _fileHandler.writeByte(0); // Byte 14
        _fileHandler.writeByte(0); // Byte 15
        _fileHandler.writeByte(0); // Byte 16
        _fileHandler.writeByte(0); // Byte 17
        _fileHandler.writeByte(0); // Byte 18
        _fileHandler.writeByte(0); // Byte 19
        _fileHandler.writeByte(0); // Byte 20
        _fileHandler.writeByte(0); // Byte 21
        _fileHandler.writeByte(0); // Byte 22
        _fileHandler.writeByte(0); // Byte 23
        _fileHandler.writeByte(0); // Byte 24
        _fileHandler.writeByte(0); // Byte 25
        _fileHandler.writeByte(0); // Byte 26
        _fileHandler.writeByte(0); // Byte 27
        _fileHandler.writeByte(0); // Byte 28
        _fileHandler.writeByte(0); // Byte 29
        _fileHandler.writeByte(0); // Byte 30
        _fileHandler.writeByte(0); // Byte 31
    }

    /**
     * Iterates through the DbfTableModel's collection of columns and calls the
     * writeFieldDescriptor method for each column
     * 
     * @param model The DbfTableModel
     */
    private void writeFieldDescriptors(DbfTableModel model) throws IOException {
        int columnCount = model.getColumnCount();
        for (int i = 0; i <= columnCount - 1; i++) {
            String name = model.getColumnName(i);
            int length = model.getLength(i);
            byte decimalCount = model.getDecimalCount(i);
            byte type = model.getType(i);
            writeFieldDescriptor(name, type, length, decimalCount);
        }
        _fileHandler.writeByte(13);
    }

    /**
     * Writes records to the LittleEndianOutputStream
     * 
     * @param name The field name
     * @param type The field type
     * @param length The field length
     * @param decimalPlaces The number of decimal places for each field
     */
    private void writeFieldDescriptor(String name, byte type, int length,
                                      byte decimalPlaces) throws IOException {
        _fileHandler.writeString(name, 11); // Byte 0-10
        _fileHandler.writeByte(type); // Byte 11
        _fileHandler.writeByte(0); // Byte 12 Field data address(0)
        _fileHandler.writeByte(0); // Byte 13 Field data address(1)
        _fileHandler.writeByte(0); // Byte 14 Field data address(2)
        _fileHandler.writeByte(0); // Byte 15 Field data address(3)
        _fileHandler.writeByte(length); // Byte 16 Field length in bytes
        _fileHandler.writeByte(decimalPlaces); // Byte 17 Field decimal
        // places
        _fileHandler.writeByte(0); // Byte 18 Reserved for dBASE III PLUS on
        // a LAN(0)
        _fileHandler.writeByte(0); // Byte 19 Reserved for dBASE III PLUS on
        // a LAN(1)
        _fileHandler.writeByte(0); // Byte 20 Work area 1D
        _fileHandler.writeByte(0); // Byte 21 Reserved for dBASE III PLUS on
        // a LAN(0)
        _fileHandler.writeByte(0); // Byte 22 Reserved for dBASE III PLUS on
        // a LAN(1)
        _fileHandler.writeByte(0); // Byte 23 SET FIELDS Flag
        _fileHandler.writeByte(0); // Byte 24 Reserved Bytes(0) #24
        _fileHandler.writeByte(0); // Byte 25 Reserved Bytes(0) #25
        _fileHandler.writeByte(0); // Byte 26 Reserved Bytes(0) #26
        _fileHandler.writeByte(0); // Byte 27 Reserved Bytes(0) #27
        _fileHandler.writeByte(0); // Byte 28 Reserved Bytes(0) #28
        _fileHandler.writeByte(0); // Byte 29 Reserved Bytes(0) #29
        _fileHandler.writeByte(0); // Byte 30 Reserved Bytes(0) #30
        _fileHandler.writeByte(0); // Byte 31 Reserved Bytes(0) #31
    }

    public void writeRecords(DbfTableModel model) throws IOException {

        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ENGLISH);
        df.setDecimalFormatSymbols(dfs);
        df.setGroupingUsed(false);
        
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        for (int r = 0; r <= rowCount - 1; r++) {
            _fileHandler.writeByte(32);
            for (int c = 0; c <= columnCount - 1; c++) {
                byte type = model.getType(c);
                int columnLength = model.getLength(c);
                int numDecSpaces = model.getDecimalCount(c);
                df.setMaximumFractionDigits(numDecSpaces);
                df.setGroupingUsed(false);
                String value = DbfTableModel.getStringForType(model.getValueAt(r, c),
                        type,
                        df, columnLength);

                int length = model.getLength(c);
                _fileHandler.writeString(value, length);
            }
        }
    }

    public void close() throws IOException {
        _fileHandler.writeByte(26);
        _fileHandler.close();
    }
}
