package com.altimit_server.types;

import java.io.Serializable;
import java.util.UUID;

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

    public UUID ownerUUID;

    public boolean isSceneObject = false;

    /**
     * The constructor to create a network object that has tracking of position and rotation.
     * @param viewId The view id of the objects AltimitView.
     * @param clientId The client id that created this network object.
     * @param position The world position of this network object.
     * @param rotation The world rotation of the network object
     * @param prefabName The name of the prefab this object represents.
     */
    public UnityObject(int viewId, UUID ownerUUID, int clientId, Vector3 position, Quaternion rotation, String prefabName) {
        this.viewId = viewId;
        this.clientId = clientId;
        this.ownerUUID = ownerUUID;
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
    public UnityObject(int viewId, UUID ownerUUID, int clientId, String prefabName) {
        this.viewId = viewId;
        this.ownerUUID = ownerUUID;
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
    public UnityObject(int viewId, UUID ownerUUID, int clientId, Quaternion rotation, String prefabName) {
        this.viewId = viewId;
        this.ownerUUID = ownerUUID;
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
    public UnityObject(int viewId, UUID ownerUUID, int clientId, Vector3 position, String prefabName) {
        this.viewId = viewId;
        this.ownerUUID = ownerUUID;
        this.clientId = clientId;
        this.position = position;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a
     * @param viewId
     */
    public UnityObject(int viewId){
        this.viewId = viewId;
        isSceneObject = true;
    }
}
