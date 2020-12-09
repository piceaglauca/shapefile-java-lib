package com.piceadev.shapefile.internal;

import java.util.ArrayList;
import java.io.IOException;

/**
 * Class to manage ESRI SHX data.
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class ShxFile extends EsriFile {
    private String filename;
    private ArrayList<EsriIndex> indices;

    protected ShxFileHandler fileHandler;

    /**
     * Constructor.
     *
     * @param   filename    File to use. Assumed to have .shx extension.
     */
    public ShxFile (String filename) throws IOException {
        this.filename = filename;
        fileHandler = new ShxFileHandler (filename);
        super.setFileHandler (fileHandler);

        getHeaderInfo ();

        this.indices = fileHandler.getAllIndices ();

        fileHandler.close ();
    }

    /*
    public int addFeature (EsriPoint point) {
        fileHandler = new ShxFileHandler (filename);

        int index = indices.add (indices.size() + 1, point);
        fileHandler.addFeature (this, index, point);

        fileHandler.close ();

        return index;
    }
    */
}
