package com.altimit_server.types;

import java.io.Serializable;

/**
 * Representation of 3D vectors and points.
 */
public class Vector3 implements Serializable{

    /** The X axis of the vector **/
    public float x;
    /** The Y axis of the vector **/
    public float y;
    /** The Z axis of the vector **/
    public float z;

    /**
     * Creates a new vector with given x, y, z components.
     * @param x The x axis of the vector.
     * @param y The x axis of the vector.
     * @param z The x axis of the vector/
     */
    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
