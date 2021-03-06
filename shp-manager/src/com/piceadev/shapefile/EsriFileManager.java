package com.piceadev.shapefile;

import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import com.piceadev.shapefile.internal.Shapefile;
import com.piceadev.shapefile.internal.DbfTableModel;
import com.piceadev.shapefile.internal.ShxFile;

/**
 * Read and write to ESRI Shapefiles. Shapefiles consist of at least three 
 * files: .shp, .shx, .dbf, each with the same basename. See ESRI documentation
 * for file specifications.
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriFileManager {

    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    private Shapefile shp;
    private DbfTableModel dbf;
    private ShxFile shx;

    /**
     * Constructor.
     *
     * @param filename  Filename of the shapefile. Can include a file extension;
     *                  this manager will handle all three mandatory files.
     */
    public EsriFileManager (String filename) throws IOException {
        // if String filename contains file extension .shp, remove it
        String basename = filename;
        if (filename.endsWith (".shp")) {
            basename = FilenameUtils.removeExtension (filename);
        }

        logger.fine ("EsriFileManager working on " + basename);

        String[] exts = new String[] { ".shp", ".dbf", ".shx" };
        for (String ext : exts) {
            if (! new File (basename + ext).exists ()) {
                throw new IOException (String.format ("%s not found", basename + ext));
            }
        }

        shp = new Shapefile (basename + ".shp");
        dbf = new DbfTableModel (basename + ".dbf");
        shx = new ShxFile (basename + ".shx");
    }

    // TODO: maybe call this newFeature?
    /*
    public void addFeature (EsriPoint point) {
        int recordNumber = shx.addFeature (point);
        shp.addFeature (point);
        //dbf.addFeature (point);
    }
    */
}
