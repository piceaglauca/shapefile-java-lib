package com.piceadev.shapefile.internal;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import com.piceadev.shapefile.internal.EsriConstants;
import com.piceadev.shapefile.internal.EsriFileHandler;
import com.piceadev.shapefile.internal.EsriIndex;

public class ShxFileHandler extends EsriFileHandler {
    private final Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    public ShxFileHandler (String filename) throws IOException {
        super(filename);
    }

    public ArrayList<EsriIndex> getAllIndices () throws IOException {
        this.seek (EsriConstants.HEADER_END); // seek to end of header and start of contents

        ArrayList<EsriIndex> indices = new ArrayList<>();
        EsriIndex nextIndex = null;

        nextIndex = getNextIndex (0);
        while (nextIndex != null) {
            indices.add (nextIndex);
            nextIndex = getNextIndex (indices.size ());
        }

        return indices;
    }

    private EsriIndex getNextIndex (int currentSize) throws IOException {
        EsriIndex nextIndex = new EsriIndex ();

        try {
            nextIndex.setRecordNumber (currentSize + 1);
            nextIndex.setOffset (this.readInt());
            nextIndex.setContentLength (this.readInt());
        } catch (EOFException e) {
            logger.log (Level.FINE, "Found end of file.");
            return null;
        }

        return nextIndex;
    }

    /*
    public void addFeature (ShxFile shx, int index, EsriPoint point) {
        // update header
        super(shx, index, point);

        this.seek (end of file);
        this.writeInt(offset);
        this.writeInt(contentLength);
    }
    */
}
