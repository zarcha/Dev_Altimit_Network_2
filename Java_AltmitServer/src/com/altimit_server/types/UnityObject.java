package com.altimit_server.types;

import java.io.Serializable;

/**
 * UnityObject is used for storing network object data
 */
public class UnityObject implements Serializable {
    /** The view id of the objects AltimitView. */
    public int viewId;
    /** The room id this network object resides in. **/
    /** The client id that created this network object. **/
    public int clientId;
    /** The world position of this network object. **/
    public Vector3 position;
    /** The world rotation of the network object. **/
    public Quaternion rotation;
    /** The name of the prefab this object. **/
    public String prefabName;

    /**
     * The constructor to create a network object that has tracking of position and rotation.
     * @param viewId The view id of the objects AltimitView.
     * @param clientId The client id that created this network object.
     * @param position The world position of this network object.
     * @param rotation The world rotation of the network object
     * @param prefabName The name of the prefab this object represents.
     */
    public UnityObject(int viewId, int clientId, Vector3 position, Quaternion rotation, String prefabName) {
        this.viewId = viewId;
        this.clientId = clientId;
        this.position = position;
        this.rotation = rotation;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a network object without tracking its position and rotation
     * @param viewId The view id of the objects AltimitView.
     * @param clientId The client id that created this network object.
     * @param prefabName The name of the prefab this object represents.
     */
    public UnityObject(int viewId, int clientId, String prefabName) {
        this.viewId = viewId;
        this.clientId = clientId;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a network object that only has rotation tracking
     * @param viewId The view id of the objects AltimitView.
     * @param clientId The client id that created this network object.
     * @param rotation The world rotation of the network object.
     * @param prefabName The name of the prefab this object represents.
     */
    public UnityObject(int viewId, int clientId, Quaternion rotation, String prefabName) {
        this.viewId = viewId;
        this.clientId = clientId;
        this.rotation = rotation;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a network object that only has position tracking
     * @param viewId The view id of the objects AltimitView.
     * @param clientId The client id that created this network object.
     * @param position The world position of this network object.
     * @param prefabName The name of the prefab this object represents.
     */
    public UnityObject(int viewId, int clientId, Vector3 position, String prefabName) {
        this.viewId = viewId;
        this.clientId = clientId;
        this.position = position;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a
     * @param viewId
     * @param prefabName
     */
    public UnityObject(int viewId, String prefabName){
        this.viewId = viewId;
        this.prefabName = prefabName;
    }
}
