using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Altimit;

public class TestRPCScript : MonoBehaviour {
	// Use this for initialization
	void Start () {
        RunRPC();
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void RunRPC()
    {
        if (GetComponent<AltimitView>().isMine)
        {
            Debug.Log("Going to send RPC");
            int viewID = GetComponent<AltimitView>().viewID;
            AltimitRPC.Call(RPCType.ALL, viewID, "TestRPCFunction");
        }
    }

    [AltimitRPC]
    public void TestRPCFunction()
    {
        Debug.Log("My ViewID: " + GetComponent<AltimitView>().viewID);
    }
}
