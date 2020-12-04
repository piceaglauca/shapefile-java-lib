package com.piceadev.shapefile.internal;

import com.piceadev.shapefile.internal.EsriFeature;

public class EsriPoint extends EsriFeature {
    double x, y;

    public EsriPoint () {
    }

    public EsriPoint (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX (double x) {
        this.x = x;
    }

    public void setY (double y) {
        this.y = y;
    }

    public double getX () {
        return this.x;
    }

    public double getY () {
        return this.y;
    }
}
