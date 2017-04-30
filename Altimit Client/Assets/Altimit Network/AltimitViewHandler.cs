using UnityEngine;
using System.Collections.Generic;

namespace Altimit
{
    public class AltimitViewHandler : MonoBehaviour
    {
        public static int MAX_VIEWS = 1000;
        public static Dictionary<int, GameObject> NetworkObjects = new Dictionary<int, GameObject>();


        public static void RegisterNetworkObject(GameObject netObject, int viewId)
        {
            int viewID;
            if (!netObject.GetComponent<AltimitView>().sceneView)
            {
                viewID = viewId;

                if (AltimitRoom.RoomOwner)
                {

                }
            }
            else
            {
                viewID = AltimitPlayer.ID * AltimitViewHandler.MAX_VIEWS + viewId;
            }

            NetworkObjects.Add(viewID, netObject);
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