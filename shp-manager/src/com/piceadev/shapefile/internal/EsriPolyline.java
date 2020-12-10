package com.piceadev.shapefile.internal;

import java.util.ArrayList;
import com.piceadev.shapefile.internal.EsriFeature;
import com.piceadev.shapefile.internal.EsriPoint;

/**
 * Class to represent an ESRI Polyline feature. From the ESRI specifications:
 *      A Polyline is an ordered set of vertices that consists of one or more 
 *      parts. A part is a connected sequence of two or more points (EsriPoint).
 *      Parts may or may not be connected to one another. Parts may or may
 *      not intersect one another.
 *
 *      Because this specification does not forbid consecutive points with 
 *      identical coordinates, shapefile readers must handle such cases. On the
 *      other hand, the degenerate, zero length parts that might result are not
 *      allowed.
 *
 * @author      Scott Howard    <piceadev@showard.ca>
 * @version     0.1
 */
public class EsriPolyline extends EsriFeature {

    //int numParts; // number of lines within the polyline feature
    //int numPoints; // total number of vertices

    /**
     * Each element of the indices ArrayList indicates where in the 
     * vertices ArrayList the beginning of a Polyline part can be found.
     */
    ArrayList<Integer> indices;

    /**
     * The vertices ArrayList contains all the points in the Polyline.
     */
    ArrayList<EsriPoint> vertices;

    /**
     * Constructor.
     *
     * @param numParts  Number of parts this Polyline has. Used to initialize
     *                  indices array with numParts elements.
     * @param numPoints Number of vertices this Polyline has. Used to 
     *                  initialize vertices array with numPoints elements.
     */
    public EsriPolyline (int numParts, int numPoints) {
        indices = new ArrayList<>(numParts);
        vertices = new ArrayList<>(numPoints);
    }

    /**
     * Return the number of parts in this Polyline. 
     *
     * @return  int
     */
    public int getNumParts () {
        return indices.size();
    }

    /**
     * Return the number of vertices in this Polyline
     *
     * @return  int
     */
    public int getNumPoints () {
        return vertices.size();
    }

    /*
    public void setNumParts (int numParts) {
        this.numParts = numParts;
    }

    public void setNumPoints (int numPoints) {
        this.numPoints = numPoints;
    }
    */

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
