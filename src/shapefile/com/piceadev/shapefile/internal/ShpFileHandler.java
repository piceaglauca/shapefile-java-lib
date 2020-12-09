package com.piceadev.shapefile.internal;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import com.piceadev.shapefile.internal.EsriFeatureFactory;
import com.piceadev.shapefile.internal.EsriConstants;
import com.piceadev.shapefile.internal.Shapefile;
import com.piceadev.shapefile.internal.EsriFileHandler;

/**
 * Class to handle read/write operations to ESRI SHP file.
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class ShpFileHandler extends EsriFileHandler {

    private final Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    /**
     * Constructor.
     *
     * @param   filename    File to use. Assumed to have .shp extension.
     */
    public ShpFileHandler (String filename) throws IOException {
        super(filename);
    }

    /**
     * Read the file and interpret all features into memory.
     *
     * @return  all features in SHP file.
     */
    public ArrayList<EsriFeature> getAllFeatures () throws IOException {
        this.seek (EsriConstants.HEADER_END); // seek to end of header and start of contents

        ArrayList<EsriFeature> features = new ArrayList<>();
        EsriFeature nextFeature = null;

        nextFeature = getNextFeature ();
        while (nextFeature != null) {
            features.add (nextFeature);
            nextFeature = getNextFeature ();
        }

        return features;
    }

    /**
     * Read the file and get the next feature in the file.
     *
     * @return  a single feature, which will be a subclass of EsriFeature.
     */
    private EsriFeature getNextFeature () throws IOException {
        EsriFeature nextFeature = null;

        try {
            nextFeature = EsriFeatureFactory.getFeature (this);
        } catch (EOFException e) {
            logger.log (Level.FINE, "Found end of file.");
            return null;
        }

        return nextFeature;
    }

}
