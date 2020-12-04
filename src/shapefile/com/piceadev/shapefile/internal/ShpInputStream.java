package com.piceadev.shapefile.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import com.piceadev.shapefile.internal.EsriFeatureFactory;
import com.piceadev.shapefile.internal.Shapefile;
import com.piceadev.shapefile.internal.LittleEndianInputStream;

public class ShpInputStream {

    LittleEndianInputStream leis;
    int shapeType;
    int fileLength; // in 16-bit words, including 100 byte header

    public ShpInputStream (String basename) 
        throws IOException {

        FileInputStream fis = new FileInputStream (basename + ".shp");
        BufferedInputStream bis = new BufferedInputStream (fis);
        leis = new LittleEndianInputStream (bis);
    }

    public ShpInputStream (File file) 
        throws IOException {

        FileInputStream fis = new FileInputStream (file);
        BufferedInputStream bis = new BufferedInputStream (fis);
        leis = new LittleEndianInputStream (bis);
    }

   /**
    * Reads the header section of a .shp file
    *
    * @return the shape type
    */
   public int readHeader()
         throws IOException {
      /* int fileCode = */leis.readInt();
      leis.skipBytes(20); // unused bytes in header
      fileLength = leis.readInt(); // in 16-bit words, including 100 byte header
      /* int version = */leis.readLEInt();
      shapeType = leis.readLEInt();
      /* double xMin = */leis.readLEDouble();
      /* double yMin = */leis.readLEDouble();
      /* double xMax = */leis.readLEDouble();
      /* double yMax = */leis.readLEDouble();
      /* double zMin = */leis.readLEDouble();
      /* double zMax = */leis.readLEDouble();
      /* double mMin = */leis.readLEDouble();
      /* double mMax = */leis.readLEDouble();
      return shapeType;
    }

    public Shapefile readContents () 
        throws IOException {

        Shapefile shp = new Shapefile (shapeType);

        EsriFeature feature = getNextFeature();
        while (feature != null) {
            shp.addFeature (feature);
            feature = getNextFeature();
        }

        return shp;
    }

    private EsriFeature getNextFeature () 
        throws IOException {

        return EsriFeatureFactory.getFeature (leis);
    }

}