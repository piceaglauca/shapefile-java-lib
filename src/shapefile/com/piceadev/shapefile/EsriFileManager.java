package com.piceadev.shapefile;

import org.apache.commons.io.FilenameUtils;
import com.piceadev.shapefile.internal.Shapefile;
import com.piceadev.shapefile.internal.DbfTableModel;
import com.piceadev.shapefile.internal.ShxFile;

public class EsriFileManager {
    private Shapefile shp;
    private DbfTableModel dbf;
    private ShxFile shx;

    public EsriFileManager (String filename) {
        // if String filename contains file extension .shp, remove it
        baseFileName = filename;
        if (filename.endsWith (".shp")) {
            baseFileName = FilenameUtils.removeExtension (filename);
        }

        for (String ext : {".shp", ".dbf", ".shx"} ) {
            if (! new File (basename + ext).exists ()) {
                throw new IOException (String.format ("%s not found", basename + ext));
            }
        }

        shp = new Shapefile (basename + ".shp");
        dbf = new dbfTable (basename + ".dbf");
        shx = new ShxFile (basename + ".shx");
    }

    public void addFeature (EsriPoint point) {
        int recordNumber = shx.addFeature (point);
        shp.addFeature (point);
        dbf.addFeature (point);
    }
}
