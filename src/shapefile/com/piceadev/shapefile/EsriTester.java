package com.piceadev.shapefile;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import com.piceadev.shapefile.EsriFileManager;

public class EsriTester {

    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    private static void configLogger () {
        // Set up debug logging
        System.setProperty ("java.util.logging.SimpleFormatter.format", "%1$tH:%1$tM:%1$tS %4$-6s: %5$s%n");
        Handler handler = new ConsoleHandler ();
        handler.setLevel (Level.ALL);
        logger.addHandler (handler);
        logger.setLevel (Level.FINEST);
        logger.setUseParentHandlers (false);
    }

    public static void main (String[] args) {
        configLogger();

        String filename = args[0];
        logger.fine (String.format ("Opening %s", filename));

        try {
            EsriFileManager manager = new EsriFileManager (filename);
        } catch (IOException e) {
            String stackTrace = "\tat " + e.getStackTrace()[0].toString();
            for (int i = 1 ; i < e.getStackTrace().length ; i++)
                stackTrace = stackTrace.concat ("\n\tat " + e.getStackTrace ()[i].toString());

            logger.severe (String.format ("%s\n%s", e.toString(), stackTrace));
        }
    }
}
