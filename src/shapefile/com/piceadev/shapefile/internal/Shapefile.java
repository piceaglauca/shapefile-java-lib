package com.piceadev.shapefile.internal;

import java.util.ArrayList;
import com.piceadev.shapefile.internal.EsriFeature;

public class Shapefile {
    private ArrayList<EsriFeature> features;
    private int shapeType;

    public Shapefile (int shapeType) {
        this.shapeType = shapeType;
    }

    protected void addFeature (EsriFeature feature) {
        this.features.add (feature);
    }
}
