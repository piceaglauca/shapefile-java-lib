package com.piceadev.shapefile;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import com.piceadev.shapefile.internal.ShpInputStream;
import com.piceadev.shapefile.internal.Shapefile;

public class ShapefileReader {

    private String baseFileName;
    private final Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    public ShapefileReader(String filename) 
        throws IOException {
        // set up the logger
        logger.setLevel (Level.FINEST);

        // if String filename contains file extension .shp, remove it
        String basename = filename;
        if (filename.endsWith (".shp")) {
            basename = FilenameUtils.removeExtension (filename);
        }

        File shpFile = new File (basename + ".shp");
        File shxFile = new File (basename + ".shx");
        File dbfFile = new File (basename + ".dbf");

        // if one of the mandatory files are not present (shp, shx, dbf), throw exception
        if (! shpFile.exists()) {
            throw new IOException (String.format ("%s.shp not found", basename));
        }
        if (! shxFile.exists()) {
            throw new IOException (String.format ("%s.shx not found", basename));
        }
        if (! dbfFile.exists()) {
            throw new IOException (String.format ("%s.dbf not found", basename));
        }
    }

    public Shapefile openShapefile() 
        throws IOException {

        ShpInputStream shpis = new ShpInputStream (baseFileName + ".shp");
        int shapeType = shpis.readHeader();
        Shapefile shp = shpis.readContents();

        // TODO: read dbf file

        return shp;
    }
}
