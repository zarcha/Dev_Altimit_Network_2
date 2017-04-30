package com.altimit.hazelcast;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

/**
 * This is a class to hold Room information.
 */
public class Room implements Serializable{

    /** The id of the user that is currently the master client. **/
    public int ownerId;
    /** The max number of users that are allowed to be in the room. **/
    public int max_users;
    /** A list of user id that are in the room **/
    public HashMap<UUID, User> users = new HashMap<>();
    /** A list of network objects related to the room **/
    public HashMap<Integer, UnityObject> unityObjects = new HashMap<>();

    /**
     * The constructor to create a room with no max amount of users
     * @param owner The id of the user that is currently the master client.
     */
    public Room(User owner){
        this.ownerId = owner.id;
        users.put(owner.uuid, owner);
    }

    /**
     * The constructor to create a room with a max amount of users
     * @param owner The id of the user that is currently the master client.
     * @param max_users The max number of users that are allowed to be in the room.
     */
    public Room(User owner, int max_users){
        this.ownerId = owner.id;
        this.max_users = max_users;
        users.put(owner.uuid, owner);
    }

    public void AddNetworkObject(int viewId, UnityObject netObject){
        if (!unityObjects.containsKey(viewId)) {
            unityObjects.put(viewId, netObject);
            System.out.println(unityObjects);
        }
    }

    public boolean UpdateUnityObject(int viewId, Vector3 position){
        if(unityObjects.containsKey(viewId)){
            System.out.println("in room object");
            unityObjects.get(viewId).position = position;
            return true;
        }
        return false;
    }

    public boolean UpdateUnityObject(int viewId, Quaternion rotation){
        if(unityObjects.containsKey(viewId)){
            unityObjects.get(viewId).rotation = rotation;
            return true;
        }
        return false;
    }

    public boolean UpdateUnityObject(int viewId, Vector3 position, Quaternion rotation){
        if(unityObjects.containsKey(viewId)){
            UnityObject temp = unityObjects.get(viewId);
            temp.position = position;
            temp.rotation = rotation;
            unityObjects.replace(viewId, temp);
            return true;
        }
        return false;
    }
}

