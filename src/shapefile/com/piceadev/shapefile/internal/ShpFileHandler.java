package com.piceadev.shapefile.internal;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import com.piceadev.shapefile.internal.EsriFeatureFactory;
import com.piceadev.shapefile.internal.EsriConstants;
import com.piceadev.shapefile.internal.Shapefile;
import com.piceadev.shapefile.internal.EsriFileHandler;

public class ShpFileHandler extends EsriFileHandler {

    private final Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    public ShpFileHandler (String filename) throws IOException {
        super(filename);
    }

    public ArrayList<EsriFeature> getAllFeatures () throws IOException {
        this.seek (EsriConstants.HEADER_END); // seek to end of header and start of contents

        ArrayList<EsriFeature> features = new ArrayList<>();
        EsriFeature nextFeature = null;

        nextFeature = getNextFeature ();
        while (feature != null) {
            features.add (feature.getRecordNumber(), feature);
            feature = getNextFeature ();
        }

        return features;
    }

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
