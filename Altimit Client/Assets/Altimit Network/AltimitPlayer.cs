using UnityEngine;
using System.Collections;
using Altimit;

namespace Altimit {
	public class AltimitPlayer : MonoBehaviour {

		private static string username;
		public static string Username {
			get{
				return username;
			}
			set {
				AltimitNetwork.Send ("SetUsername", value);
				username = value;
			}
		}

		private static int id = 0;
		public static int ID {
			get{
				return id;
			}
		}

		//<summary>
		// SERVER CALL ONLY! Dont set on client.
		//</summary>
		[AltimitRPC]
		private void SetId(int newId){
			id = newId;
		}
	}
}
