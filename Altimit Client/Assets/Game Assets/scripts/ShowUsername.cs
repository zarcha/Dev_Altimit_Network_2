using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ShowUsername : MonoBehaviour {

	// Use this for initialization
	void Update () {
        //this.gameObject.GetComponentInParent<AltimitView>().ownerName;
        GetComponent<TextMesh>().text = GetComponentInParent<AltimitView>().ownerName;

        GameObject cam = GameObject.FindGameObjectWithTag("MainCamera");

        if (!GetComponentInParent<AltimitView>().isMine && cam)
        {
            transform.LookAt(cam.transform.position);
        }
    }
}
