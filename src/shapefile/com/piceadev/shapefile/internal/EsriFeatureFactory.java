package com.piceadev.shapefile.internal;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.piceadev.shapefile.internal.EsriFeature;
import com.piceadev.shapefile.internal.EsriPoint;
import com.piceadev.shapefile.internal.EsriPolyline;
import com.piceadev.shapefile.internal.EsriPolygon;
import com.piceadev.shapefile.internal.LittleEndianInputStream;

public class EsriFeatureFactory { 

    private final static Logger logger = Logger.getLogger ("com.piceadev.shapefile");

    public static EsriFeature getFeature (LittleEndianInputStream leis) 
        throws IOException {
        // record header
        int recordNumber = leis.readInt();
        int recordLength = leis.readInt(); // in 16-bit words

        if (recordLength == -1) {
            return null; // end of file
        }

        // record content
        int recordShapeType = leis.readLEInt(); // starts with shape type, which must be same as header or null
        //if (recordShapeType != 0 && recordShapeType != shapeType)
        //    throw Exception ("invalid shapefile");

        int contentsLength = recordLength - 8 - 4; // 8 bytes for the header, 4 bytes for the shape type

        switch (recordShapeType) {
            case 1: // Point
                logger.log (Level.FINE, String.format ("Feature %d is a Point feature of length %d", recordNumber, recordLength));
                return getPointFeature (leis);
            case 3: // Polyline
                logger.log (Level.FINE, String.format ("Feature %d is a Polyline feature of length %d", recordNumber, recordLength));
                return getPolylineFeature (leis, contentsLength);
            //case 5: // Polygon
                //return getPolygonFeature (leis, contentsLength);
            default:
                return null; // invalid shapetype or unhandled by this EsriFeatureFactory
        }
    }

    private static EsriPoint getPointFeature (LittleEndianInputStream leis) 
        throws IOException {
        EsriPoint point = new EsriPoint();

        point.setX (leis.readLEDouble());
        point.setY (leis.readLEDouble());

        logger.log (Level.FINE, String.format ("Found Point with coords %f x %f", point.getX (), point.getY ()));

        return point;
    }

    private static EsriPolyline getPolylineFeature (LittleEndianInputStream leis, int contentsLength) 
        throws IOException {
        EsriPolyline line = new EsriPolyline();

        /* double xMin = */leis.readLEDouble();
        /* double yMin = */leis.readLEDouble();
        /* double xMax = */leis.readLEDouble();
        /* double yMax = */leis.readLEDouble();
        int numParts = leis.readLEInt();
        int numPoints = leis.readLEInt();

        logger.log (Level.FINE, String.format ("Polyline feature has %d parts and %d total vertices", numParts, numPoints));
        line.setNumParts (numParts);
        line.setNumPoints (numPoints);

        for (int part = 0; part < numParts; part++) {
            int index = leis.readLEInt();
            line.addPartIndex (part, index);

            logger.log (Level.FINE, String.format ("Part %d starts at vertex %d", part, index));
        }

        for (int point = 0; point < numPoints; point++) {
            double x = leis.readLEDouble ();
            double y = leis.readLEDouble ();
            line.addPoint (point, x, y);

            logger.log (Level.FINE, String.format ("Vertex %d coords %f x %f", point, x, y));
            // or, 
            // line.addPoint (point, new EsriPoint (leis.readLEDouble (), leis.readLEDouble ())); // dbl X, dbl Y
        }

        // if (line.validate ()) {} // check for repeat vertices and remove, and verify a non-zero length in each part

        return line;
    }

    // this case is complicated and is not currently worth completing (for com.piceadev.gpssurvey).
    // The specification for a Polygon requires certain orders, and for non-self-intersecting.
    //
    // https://www.esri.com/library/whitepapers/pdfs/shapefile.pdf
    //
    //private static EsriPolygon getPolygonFeature (LittleEndianInputStream leis, int contentsLength) {
    //    EsriPolygon polygon = new EsriPolygon ();
    //
    //    /* double xMin = */leis.readLEDouble();
    //    /* double yMin = */leis.readLEDouble();
    //    /* double xMax = */leis.readLEDouble();
    //    /* double yMax = */leis.readLEDouble();
    //    int numParts = leis.readLEInt();
    //    int numPoints = leis.readLEInt();
    //
    //    polygon.setNumParts (numParts);
    //    polygon.setNumPoints (numPoints);
    //
    //    for (int part = 0; part < numParts; part++) {
    //        polygon.addPartIndex (part, leis.readLEInt());
    //    }
    //
    //    for (int point = 0; point < numPoints; point++) {
    //        polygon.addPoint (point, leis.readLEDouble(), leis.readLEDouble ()); // addPoint (int idx, dbl X, dbl Y)
    //        // or, 
    //        // polygon.addPoint (point, new EsriPoint (leis.readLEDouble (), leis.readLEDouble ())); // dbl X, dbl Y
    //    }
    //
    //    //if (polygon.validate ()) {} // check for repeat vertices and remove, verify non-self-intersecting, and verify a non-zero length and non-zero area in each part
    //
    //    return polygon;
    //}

}
