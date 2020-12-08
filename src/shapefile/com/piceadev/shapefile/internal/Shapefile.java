package com.piceadev.shapefile.internal;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.io.IOException;
import com.piceadev.shapefile.internal.EsriFeature;

public class Shapefile extends EsriFile {

    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    private ArrayList<EsriFeature> features;
    private String filename;

    protected ShpFileHandler fileHandler;

    // TODO: what if it's a new file? 
    public Shapefile (String filename) throws IOException {
        this.filename = filename;
        fileHandler = new ShpFileHandler (filename);
        super.setFileHandler (fileHandler);

        logger.fine ("fileHandler is of type " + fileHandler.getClass());

        this.getHeaderInfo ();

        // read contents
        this.features = fileHandler.getAllFeatures ();

        fileHandler.close();
    }

    /*
    public void addFeature (EsriPoint point) {
        fileHandler = new ShpFileHandler (filename);

        // write new record to .shp
        fileHandler.addFeature (point);

        // update file header coordinate bounds
        if (point.getX () < xMin) {
            xMin = point.getX ();
            fileHandler.setXMin (xMin);
        } else if (point.getX () > xMax) {
            xMax = point.getX ();
            fileHandler.setXMax (xMax);
        }
        if (point.getY () < yMin) {
            yMin = point.getY ();
            fileHandler.setXMin (yMin);
        } else if (point.getY () > yMax) {
            yMax = point.getY ();
            fileHandler.setYMax (yMax);
        }
        // TODO: Z, M

        // update file header
        this.fileLength += 28; // 8 bytes for record header, 4 bytes for shape type, 8 bytes for X, 8 bytes for Y
        fileHandler.setFileLength (fileLength);

        features.add (point);

        fileHandler.close();
    }
    */
}
