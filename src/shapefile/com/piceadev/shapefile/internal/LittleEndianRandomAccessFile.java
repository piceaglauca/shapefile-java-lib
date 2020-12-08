// **********************************************************************
// 
// Adapted from BBN Technologies: com.bbn.openmap.dataAccess.shape.input.LittleEndianInputStream
//                            and com.bbn.openmap.dataAccess.shape.input.LittleEndianOutputStream
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
// $Source: /cvs/distapps/openmap/src/openmap/com/bbn/openmap/dataAccess/shape/input/LittleEndianInputStream.java,v $
// $Source: /cvs/distapps/openmap/src/openmap/com/bbn/openmap/dataAccess/shape/input/LittleEndianOutputStream.java,v $
// $RCSfile: LittleEndianInputStream.java,v $
// $RCSfile: LittleEndianOutputStream.java,v $
// $Revision: 1.6 $
// $Date: 2006/08/25 15:36:15 $
// $Author: dietrick $
// 
// **********************************************************************

package com.piceadev.shapefile.internal;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;

/**
 * Provides methods for reading data streams in Little Endian and Big Endian.
 * Adapted from the book, Java IO, Elliotte Rusty Harold, Ch. 7.
 * 
 * @author Doug Van Auken
 */
public class LittleEndianRandomAccessFile extends RandomAccessFile {

    // The output side of this file comes from LittleEndianOutputStream, which 
    // extends DataOutputStream, which contains a field for number of bytes written.
    // TODO: Not using currently, but maybe it's necessary.
    //protected int written = 0;

    /**
     * Constructor
     * 
     * @param in An input stream that this is chained to.
     */
    public LittleEndianRandomAccessFile(String filename) throws FileNotFoundException {
        super(filename, "rw");
    }

    /**
     * Constructs a string from the underlying input stream
     * 
     * @param length The length of bytes to read
     */
    public String readString(int length) throws IOException {
        byte[] array = new byte[length];
        readFully(array);
        String s = new String(array);
        return s.trim();
    }

    /**
     * Translates little endian short to big endian short
     * 
     * @return short A big endian short
     */
    public short readLEShort() throws IOException {
        int byte1 = read();
        int byte2 = read();
        if (byte2 == -1)
            throw new EOFException();
        return (short) ((byte2 << 8) + byte1);
    }

    /**
     * Translates a little endian unsigned short to big endian int
     * 
     * @return int A big endian short
     */
    public int readLEUnsignedShort() throws IOException {
        int byte1 = read();
        int byte2 = read();
        if (byte2 == -1)
            throw new EOFException();
        return (byte2 << 8) + byte1;
    }

    /**
     * Translates a little endian char into a big endian char
     * 
     * @return char A big endian char
     */
    public char readLEChar() throws IOException {
        int byte1 = read();
        int byte2 = read();
        if (byte2 == -1)
            throw new EOFException();
        return (char) ((byte2 << 8) + byte1);
    }

    /**
     * Translates a little endian int into a big endian int
     * 
     * @return int A big endian int
     */
    public int readLEInt() throws IOException {
        int byte1, byte2, byte3, byte4;
        synchronized (this) {
            byte1 = read();
            byte2 = read();
            byte3 = read();
            byte4 = read();
        }
        if (byte4 == -1) {
            throw new EOFException();
        }
        return (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    /**
     * Translates a little endian long into a big endian long
     * 
     * @return long A big endian long
     */
    public long readLELong() throws IOException {
        long byte1 = read();
        long byte2 = read();
        long byte3 = read();
        long byte4 = read();
        long byte5 = read();
        long byte6 = read();
        long byte7 = read();
        long byte8 = read();
        if (byte8 == -1) {
            throw new EOFException();
        }
        return (byte8 << 56) + (byte7 << 48) + (byte6 << 40) + (byte5 << 32) + (byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1;
    }

    public String readLEUTF() throws IOException {
        int byte1 = read();
        int byte2 = read();
        if (byte2 == -1)
            throw new EOFException();
        int numbytes = (byte1 << 8) + byte2;

        char result[] = new char[numbytes];
        int numread = 0;
        int numchars = 0;

        while (numread < numbytes) {
            int c1 = readUnsignedByte();
            int c2, c3;

            // look at the first four bits of c1 to determine how many
            // bytes in this char
            int test = c1 >> 4;
            if (test < 8) { // one byte
                numread++;
                result[numchars++] = (char) c1;
            } else if (test == 12 || test == 13) { // two bytes
                numread += 2;
                if (numread > numbytes)
                    throw new UTFDataFormatException();
                c2 = readUnsignedByte();
                if ((c2 & 0xC0) != 0x80)
                    throw new UTFDataFormatException();
                result[numchars++] = (char) (((c1 & 0x1F) << 6) | (c2 & 0x3F));
            } else if (test == 14) { // three bytes
                numread += 3;
                if (numread > numbytes)
                    throw new UTFDataFormatException();
                c2 = readUnsignedByte();
                c3 = readUnsignedByte();
                if (((c2 & 0xC0) != 0x80) || ((c3 & 0xC0) != 0x80)) {
                    throw new UTFDataFormatException();
                }
                result[numchars++] = (char) (((c1 & 0x0F) << 12) | ((c2 & 0x3F) << 6) | (c3 & 0x3F));
            } else { // malformed
                throw new UTFDataFormatException();
            }
        } // end while
        return new String(result, 0, numchars);
    }

    /**
     * Reads a little endian double into a big endian double
     * 
     * @return double A big endian double
     */
    public final double readLEDouble() throws IOException {
        return Double.longBitsToDouble(this.readLELong());
    }

    /**
     * Reads a little endian float into a big endian float
     * 
     * @return float A big endian float
     */
    public final float readLEFloat() throws IOException {
        return Float.intBitsToFloat(this.readLEInt());
    }

    public void writeString(String string, int length) throws IOException {
        if (string.length() < length) {
            String newstring = zeroFill(string, length);
            byte[] bytes = newstring.getBytes();
            //         if(length==11){
            //         System.out.println("bytes.length=" + bytes.length);
            //         }
            write(bytes);
        } else {
            String newstring = string.substring(0, length);
            byte[] bytes = newstring.getBytes();
            //        if(length==11){
            //          System.out.println("bytes.length=" + bytes.length);
            //        }
            write(bytes);
        }
    }

    private String zeroFill(String string, int length) {
        char[] oldchars = string.toCharArray();
        char[] newchars = new char[length];
        for (int i = 0; i <= newchars.length - 1; i++) {
            if (i <= oldchars.length - 1) {
                newchars[i] = oldchars[i];
            } else {
                newchars[i] = 0;
            }
        }
        return new String(newchars);
    }

    /**
     * Writes a number of type short in little endian
     * 
     * @param s A number of type short
     */
    public void writeLEShort(short s) throws IOException {
        write(s & 0xFF);
        write((s >>> 8) & 0xFF);
        //written += 2;
    }

    /**
     * Writes a number of type char in little endian param c An
     * integer that is upcast from a Char data type.
     */
    public void writeLEChar(int c) throws IOException {
        write(c & 0xFF);
        write((c >>> 8) & 0xFF);
        //written += 2;
    }

    /**
     * Writes a number of type int in little endian
     * 
     * @param i A number of type int
     */
    public void writeLEInt(int i) throws IOException {
        write(i & 0xFF);
        write((i >>> 8) & 0xFF);
        write((i >>> 16) & 0xFF);
        write((i >>> 24) & 0xFF);
        //written += 4;
    }

    /**
     * Writes a number of type long in little endian
     * 
     * @param l A number of type long
     */
    public void writeLELong(long l) throws IOException {
        write((int) l & 0xFF);
        write((int) (l >>> 8) & 0xFF);
        write((int) (l >>> 16) & 0xFF);
        write((int) (l >>> 24) & 0xFF);
        write((int) (l >>> 32) & 0xFF);
        write((int) (l >>> 40) & 0xFF);
        write((int) (l >>> 48) & 0xFF);
        write((int) (l >>> 56) & 0xFF);
        //written += 8;
    }

    /**
     * Writes a number of type float in little endian
     * 
     * @param f A number of type float.
     */
    public final void writeLEFloat(float f) throws IOException {
        this.writeLEInt(Float.floatToIntBits(f));
    }

    /**
     * Writes a number a number of type double in little endian
     * 
     * @param d A number of type double
     */
    public final void writeLEDouble(double d) throws IOException {
        this.writeLELong(Double.doubleToLongBits(d));
    }

    /**
     * Writes a String in little endian
     * 
     * @param s A string
     */
    public void writeLEChars(String s) throws IOException {
        int length = s.length();
        for (int i = 0; i < length; i++) {
            int c = s.charAt(i);
            write(c & 0xFF);
            write((c >>> 8) & 0xFF);
        }
        //written += length * 2;
    }

    public void writeLEUTF(String s) throws IOException {
        int numchars = s.length();
        int numbytes = 0;
        for (int i = 0; i < numchars; i++) {
            int c = s.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F))
                numbytes++;
            else if (c > 0x07FF)
                numbytes += 3;
            else
                numbytes += 2;
        }

        if (numbytes > 65535)
            throw new UTFDataFormatException();

        write((numbytes >>> 8) & 0xFF);
        write(numbytes & 0xFF);
        for (int i = 0; i < numchars; i++) {
            int c = s.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                write(c);
            } else if (c > 0x07FF) {
                write(0xE0 | ((c >> 12) & 0x0F));
                write(0x80 | ((c >> 6) & 0x3F));
                write(0x80 | (c & 0x3F));
                //written += 2;
            } else {
                write(0xC0 | ((c >> 6) & 0x1F));
                write(0x80 | (c & 0x3F));
                //written += 1;
            }
        }
        //written += numchars + 2;
    }
}
