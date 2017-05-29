using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityStandardAssets.Characters.FirstPerson;
using Altimit;

public class Player : MonoBehaviour {

    public GameObject camObj;

	// Use this for initialization
	void Start () {
        if (!gameObject.GetComponent<AltimitView>().isMine)
        {
            gameObject.GetComponent<FirstPersonController>().enabled = false;
            gameObject.GetComponent<CharacterController>().enabled = false;
            camObj.SetActive(false);
        }
	}

    
    [AltimitRPC]
    public void saySomething()
    {
        Debug.Log("Something");
    }
}
