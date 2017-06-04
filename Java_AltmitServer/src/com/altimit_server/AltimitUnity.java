package com.altimit_server;

import com.altimit_server.types.*;

import java.util.Arrays;
import java.util.UUID;

public class AltimitUnity {

    @AltimitCmd
    public static void Instantiate(String prefabName, Vector3 position, Quaternion rotation, Integer viewId, UUID clientUUID){
        UnityObject temp = new UnityObject(viewId, clientUUID, Users.GetId(clientUUID), position, rotation, prefabName);
        String room = Users.GetRoomName(clientUUID);
        Rooms.AddUnityObject(room, temp, clientUUID);
    }

    @AltimitCmd
    public static void UpdateUnityObject(Vector3 position, Quaternion rotation, long timeStamp, Integer viewId, UUID clientUUID){
        Rooms.UpdateRoomUnityObject(Users.GetRoomName(clientUUID), viewId, position, rotation, timeStamp, clientUUID);

    }

    @AltimitCmd
    public static void UpdateUnityObject(Vector3 position, long timeStamp, Integer viewId, UUID clientUUID){
        Rooms.UpdateRoomUnityObject(Users.GetRoomName(clientUUID), viewId, position, timeStamp, clientUUID);
    }

    @AltimitCmd
    public static void UpdateUnityObject(Quaternion rotation, long timeStamp, Integer viewId, UUID clientUUID){
        Rooms.UpdateRoomUnityObject(Users.GetRoomName(clientUUID), viewId, rotation, timeStamp, clientUUID);
    }

    @AltimitCmd
    public static void RegisterSceneObject(int viewId, Vector3 position, Quaternion rotation, UUID clientUUID){

    }

    @AltimitCmd
    public static void AltimitRPC(int _type, Object[] _parameters){
        UUID clientUUID = (UUID)_parameters[_parameters.length - 1];

        String roomName = Users.GetRoomName(clientUUID);
        UUID[] ignoreList = {clientUUID};

        switch (_type){
            //ALL
            case 0:
                PostMan.SendPost(clientUUID, "AltimitRPCInvoker", Arrays.copyOfRange(_parameters, 1, (_parameters.length - 1)));
            //OTHERS
            case 1:
                PostMan.SendPost(roomName, ignoreList, "AltimitRPCInvoker", Arrays.copyOfRange(_parameters, 1, (_parameters.length - 1)));
            //OTHERS_NO_SERVER
            case 2:
                AltimitMethod.CallAltimitMethod((String)_parameters[2], Arrays.copyOfRange(_parameters, 3, _parameters.length + 1));
                break;
        }
    }
}
