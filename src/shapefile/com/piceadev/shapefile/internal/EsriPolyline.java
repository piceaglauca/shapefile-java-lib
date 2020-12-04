package com.piceadev.shapefile.internal;

import java.util.ArrayList;
import com.piceadev.shapefile.internal.EsriFeature;
import com.piceadev.shapefile.internal.EsriPoint;

public class EsriPolyline extends EsriFeature {

    int numParts; // number of lines within the polyline feature
    int numPoints; // total number of vertices
    ArrayList<Integer> indices;
    ArrayList<EsriPoint> vertices;

    public EsriPolyline () {
        indices = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void setNumParts (int numParts) {
        this.numParts = numParts;
    }

    public void setNumPoints (int numPoints) {
        this.numPoints = numPoints;
    }

    public void addPartIndex (int partIndex, int vertexIndex) {
        indices.add (partIndex, vertexIndex);
    }

    public void addPoint (int index, double x, double y) {
        vertices.add (index, new EsriPoint (x, y));
    }
    
    public void addPoint (int index, EsriPoint point) {
        vertices.add (index, point);
    }

    /*
    public boolean validate () {
        return true;
    }
    */
}
