using UnityEngine;
using System.Collections;
using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading;

namespace Altimit {
	public class AltimitMethod {

		//<summary>
		// All methods marked with [AltimitRPC]
		//</summary>
		private List<MethodBase> altimitMethods = new List<MethodBase>();

        private static AltimitMethod instance = null;

        public static AltimitMethod GetInstance()
        {
            if(instance == null)
            {
                instance = new AltimitMethod();
            }

            return instance;
        }

		//<summary>
		// Get a list of all methods with [AltimitRPC]
		//</summary>
		public void CompileAltimitMethods(){
			Debug.Log ("Compiling all Altimit methods...");

            Thread.Sleep(1000);

            foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies()){
				foreach (Type type in assembly.GetTypes()) {
					foreach (MethodBase method in type.GetMethods(BindingFlags.NonPublic | BindingFlags.Instance)) {
						if (method.GetCustomAttributes (typeof(AltimitRPC), true).Length > 0) {
							altimitMethods.Add (method);
						}
					}
				}
			}

			Debug.Log ("Finished compiling Altimit methods...");
		}

        public bool HasLoadedMethods()
        {
            return (altimitMethods.Count > 0);
        }

        public void ClearMethods()
        {
            altimitMethods.Clear();
        }

        //<summary>
        // Comparares a method's types with the variables in an object list
        //</summary>
        public bool CompareTypes(ParameterInfo[] methodParams, object[] calledParams){
			if (methodParams.Length == calledParams.Length) {
				for (int i = 0; i < calledParams.Length; i++) {
					if (methodParams[i].ParameterType.Name != calledParams [i].GetType ().Name) {
						return false;
					}
				}
			}else{
				return false;
			}
				
			return true;
		}

		//<summary>
		// Ivokes a method by a string name and a the paramaters in a list
		//</summary>
		public void CallAltimitMethod(string methodName, params object[] paramaters){
			foreach (MethodBase m in altimitMethods) {
				if (m.Name == methodName) {
					if (CompareTypes(m.GetParameters(), paramaters)) {
						object instance = Activator.CreateInstance (m.DeclaringType);

						try {
							m.Invoke (instance, paramaters);
							return;
						} catch (Exception e) {
							Debug.Log (e.ToString ());
						}
					}
				}
			}
		}
	}
}