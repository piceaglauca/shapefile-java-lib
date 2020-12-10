package com.piceadev.shapefile.internal;

import com.piceadev.shapefile.internal.EsriFeature;

/**
 * Class to represent an ESRI Point feature.
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriPoint extends EsriFeature {
    double x, y;

    /**
     * Create EsriPoint at (x,y).
     */
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
