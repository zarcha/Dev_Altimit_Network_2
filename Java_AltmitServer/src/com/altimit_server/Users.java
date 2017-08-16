package com.altimit_server;

import com.altimit_server.types.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class is used for manipulation of user map data on hazelcast
 */
public class Users {

    /** Hazelcast map of use objects  **/
    public static Map<UUID, User> userMap = new HashMap<>();

    private static int userCount = 0;

    /**
     * Adds a user object to the Hazelcast map using the client's UUID.
     * @param clientUUID The clients UUID used as the key and stored within the object.
     */
    static void Add(UUID clientUUID){
        int tempId = userCount + 1;
        User tempUser = new User(tempId);
        tempUser.uuid = clientUUID;

        try {
            userMap.put(clientUUID, tempUser);
            /*PostMan.SendPost(clientUUID, "SetId", tempUser.id);*/
        }catch (Exception e){
            e.printStackTrace();
        }
        userCount = tempId;
    }

    /**
     * Removes a client from the Hazelcasts user map.
     * @param clientUUID Clients UUID used for removal
     */
    static void Remove(UUID clientUUID){
        if(userMap.containsKey(clientUUID)) {
            if (userMap.get(clientUUID).roomName != "") {
                Rooms.LeaveRoom(clientUUID);
            }
            userMap.remove(clientUUID);
        }
    }

    /**
     * Sets the useraname for a specific user.
     * @param clientUUID Clients UUID used to find and update the user object.
     * @param username Username to be set.
     */
    public static void SetUsername(String username, UUID clientUUID){
        User tempUser = userMap.get(clientUUID);
        tempUser.username = username;

        userMap.put(clientUUID, tempUser);
    }

    /**
     * Sets the useraname for a specific user.
     * @param clientUUID Clients UUID used to find and update the user object.
     * @param roomName Room name to be set.
     */
    static void SetRoomName(String roomName, UUID clientUUID){
        User tempUser = userMap.get(clientUUID);
        tempUser.roomName = roomName;

        userMap.put(clientUUID, tempUser);
    }

    /**
     * Retrieves the username of a specified user.
     * @param clientUUID Clients UUID used to find the user object.
     * @return Username of the user.
     */
    public static String GetUsername(UUID clientUUID){
        return userMap.get(clientUUID).username;
    }

    /**
     * Retrieves the room name the specified user is in.
     * @param clientUUID Clients UUID used to find the user object.
     * @return Room name of the user.
     */
    static String GetRoomName(UUID clientUUID){
        return userMap.get(clientUUID).roomName;
    }

    /**
     * Retrieves the Id of the specified user.
     * @param clientUUID Clients UUID used to find the user object.
     * @return Id of the user.
     */
    public static int GetId(UUID clientUUID){
        return userMap.get(clientUUID).id;
    }

    /**
     * Retrieves the user object.
     * @param clientUUID Client UUID to find the user object.
     * @return User object.
     */
    public  static User GetUser(UUID clientUUID) { return  userMap.get(clientUUID); }

    /**
     * Gets the current size of the Hazelcast user map.
     * @return int size of the user map.
     */
    public static int GetUserCount(){
        return userMap.size();
    }
}
