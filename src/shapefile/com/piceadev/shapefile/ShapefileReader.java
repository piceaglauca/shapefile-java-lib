package com.piceadev.shapefile;

import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import com.piceadev.shapefile.internal.ShpInputStream;
import com.piceadev.shapefile.internal.DbfInputStream;
import com.piceadev.shapefile.internal.Shapefile;

public class ShapefileReader {

    private String baseFileName;
    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    public ShapefileReader(String filename) 
        throws IOException {
        logger.setLevel (Level.ALL);

        // if String filename contains file extension .shp, remove it
        baseFileName = filename;
        if (filename.endsWith (".shp")) {
            baseFileName = FilenameUtils.removeExtension (filename);
        }

        logger.log (Level.FINE, String.format ("Using baseFileName: %s", baseFileName));

        File shpFile = new File (baseFileName + ".shp");
        //File shxFile = new File (baseFileName + ".shx");
        File dbfFile = new File (baseFileName + ".dbf");

        // if one of the mandatory files are not present (shp, shx, dbf), throw exception
        if (! shpFile.exists()) {
            throw new IOException (String.format ("%s.shp not found", baseFileName));
        }
        //if (! shxFile.exists()) {
        //    throw new IOException (String.format ("%s.shx not found", baseFileName));
        //}
        if (! dbfFile.exists()) {
            throw new IOException (String.format ("%s.dbf not found", baseFileName));
        }

        logger.log (Level.FINE, String.format ("All necessary files exist for %s", baseFileName));
    }

    public Shapefile openShapefile() 
        throws IOException {

        ShpInputStream shpis = new ShpInputStream (baseFileName + ".shp");
        int shapeType = shpis.readHeader();

        logger.log (Level.FINE, String.format ("Found shapefile of type %d", shapeType));

        Shapefile shp = shpis.readContents();

        // TODO: read dbf file
        DbfInputStream dbfis = new DbfInputStream (baseFileName + ".dbf");

        return shp;
    }

    public static void main (String[] args) {
        // Set up debug logging
        System.setProperty ("java.util.logging.SimpleFormatter.format", "%1$tH:%1$tM:%1$tS %4$-6s: %5$s%n");
        Handler handler = new ConsoleHandler ();
        handler.setLevel (Level.ALL);
        //SimpleFormatter formatter = handler.getFormatter ();
        //formatter.format = "%4$s: %5$s";
        logger.addHandler (handler);
        logger.setLevel (Level.FINEST);
        logger.setUseParentHandlers (false);

        String filename = args[0];
        System.out.println (String.format ("Opening %s", filename));

        try {
            ShapefileReader reader = new ShapefileReader (filename);
            reader.openShapefile ();
        } catch (IOException e) {
            String stackTrace = "\tat " + e.getStackTrace()[0].toString();
            for (int i = 1 ; i < e.getStackTrace().length ; i++)
                stackTrace = stackTrace.concat ("\n\tat " + e.getStackTrace ()[i].toString());

            logger.log (Level.SEVERE, String.format ("%s\n%s", e.toString(), stackTrace));
        }
    }
}
