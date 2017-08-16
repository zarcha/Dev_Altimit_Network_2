using UnityEngine;
using System.Collections.Generic;
using System.Threading;

namespace Altimit
{
    public class AltimitViewHandler : MonoBehaviour
    {
        public static int MAX_VIEWS = 1000;
        public static Dictionary<int, GameObject> NetworkObjects = new Dictionary<int, GameObject>();

        private static List<int> pendingSceneObjects = new List<int>();

        public static void RegisterNetworkObject(GameObject netObject, int viewId)
        {
          
            if (netObject.GetComponent<AltimitView>().sceneView)
            {
                if(AltimitRoom.RoomName == null)
                {
                    if(pendingSceneObjects.ToArray().Length <= 0)
                    {
                        new Thread(() =>
                        {
                            ManageSceneObjectsWhenInARoom();
                        }).Start();
                    }

                    pendingSceneObjects.Add(viewId);
                    Debug.Log("added a scene object");
                }
                else
                {
                    if (AltimitRoom.RoomOwner)
                    {
                        AltimitNetwork.Send("RegisterSceneObject", viewId);
                    }
                }
            }
            else if(viewId <= -1)
            {
                viewId = AltimitPlayer.ID * AltimitViewHandler.MAX_VIEWS + viewId;
            }

            NetworkObjects.Add(viewId, netObject);
        }

        private static void ManageSceneObjectsWhenInARoom()
        {
            for(;;)
            {
                if (AltimitRoom.RoomName != null)
                {
                    if (AltimitRoom.RoomOwner)
                    {
                        Debug.Log("Clearing view list");
                        foreach (int view in pendingSceneObjects)
                        {
                            AltimitNetwork.Send("RegisterSceneObject", view);
                        }
                    }

                    break;
                }
            }
        }

        [AltimitRPC]
        private void UpdateViewObject(int viewId, Vector3 position, Quaternion rotation, long timeStamp)
        {
            AltimitNetwork.tasker.ScheduleTask(new Task(delegate
            {
                if (NetworkObjects.ContainsKey(viewId))
                {
                    GameObject temp = NetworkObjects[viewId];
                    Component[] tempComp = temp.GetComponents<AltimitView>();

                    foreach (AltimitView view in tempComp)
                    {
                        if (view.viewID == viewId)
                        {
                            view.UpdatePositionRotation(position, rotation, timeStamp);
                        }
                    }
                }
            }));
        }

        [AltimitRPC]
        private void UpdateViewObject(int viewId, Vector3 position, long timeStamp)
        {
            AltimitNetwork.tasker.ScheduleTask(new Task(delegate
            {
                if (NetworkObjects.ContainsKey(viewId))
                {
                    GameObject temp = NetworkObjects[viewId];
                    Component[] tempComp = temp.GetComponents<AltimitView>();

                    foreach (AltimitView view in tempComp)
                    {
                        if (view.viewID == viewId)
                        {
                            view.UpdatePositionRotation(position, timeStamp);
                        }
                    }
                }
            }));
        }

        [AltimitRPC]
        private void UpdateViewObject(int viewId, Quaternion rotation, long timeStamp)
        {
            AltimitNetwork.tasker.ScheduleTask(new Task(delegate
            {
                if (NetworkObjects.ContainsKey(viewId))
                {
                    GameObject temp = NetworkObjects[viewId];
                    Component[] tempComp = temp.GetComponents<AltimitView>();

                    foreach (AltimitView view in tempComp)
                    {
                        if (view.viewID == viewId)
                        {
                            view.UpdatePositionRotation(rotation, timeStamp);
                        }
                    }
                }
            }));
        }
    }
}