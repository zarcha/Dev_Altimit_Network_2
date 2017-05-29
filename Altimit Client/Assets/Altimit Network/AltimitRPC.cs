using System;
using System.Reflection;
using System.Collections.Generic;
using UnityEngine;

namespace Altimit {
	//<summary>
	// Attribute used to mark methods that can be called by the server.
	//</summary>
	public class AltimitRPC : Attribute {

        public static void Call(RPCType _type, int _viewID, String _methodName, params object[] _parameters)
        {
            List<System.Object> parameters = new List<System.Object>(_parameters);
            parameters.Insert(0, _methodName);
            parameters.Insert(0, _viewID);
            parameters.Insert(0, (int)_type);

            AltimitNetwork.Send("AltimitRPC", parameters.ToArray());
        }

        [AltimitRPC]
        private void AltimitRPCInvoker(params System.Object[] _parameters)
        {

            List<System.Object> parameters = new List<System.Object>(_parameters);

            int viewId = (int)parameters[0];
            String methodName = (String)parameters[1];

            parameters.RemoveRange(0, 2);

            if (AltimitViewHandler.NetworkObjects.ContainsKey(viewId))
            {
                AltimitNetwork.tasker.ScheduleTask(new Task(delegate
                {
                    GameObject networkObject = AltimitViewHandler.NetworkObjects[viewId];
                    Component[] components = networkObject.GetComponents<MonoBehaviour>();

                    Debug.Log("Method Name: " + methodName);

                    foreach (Component c in components)
                    {
                        MethodInfo method = c.GetType().GetMethod(methodName);
                        if (method != null)
                        {
                            method.Invoke(c, parameters.ToArray());
                        }
                    }
                }));                
            }
        }
    }
}