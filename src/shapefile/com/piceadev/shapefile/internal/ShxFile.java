package com.piceadev.shapefile.internal;

public class ShxFile extends EsriFile {
    private String filename;
    private ArrayList<EsriIndex> indices;

    public ShxFile (String filename) throws IOException {
        this.filename = filename;
        fileHandler = new ShxFileHandler (filename);

        getHeaderInfo (fileHandler);

        this.indices = fileHandler.getAllIndices ();

        fileHandler.close ();
    }

    public int addFeature (EsriPoint point) {
        fileHandler = new ShxFileHandler (filename);

        int index = indices.add (indicies.size() + 1, point);
        fileHandler.addFeature (this, index, point);

        fileHandler.close ();

        return index;
    }
}
