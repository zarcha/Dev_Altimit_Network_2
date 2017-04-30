using UnityEngine;
using System.Collections.Generic;
using Altimit;
using System;

public class AltimitView : MonoBehaviour {

    public TrackState trackState;

	public int subId;
	public int ownerID;

	public bool sceneView = false;

    long lastUpdate = 0L;
    long currentTime = 0L;

    Vector3 oldPosition;
    Quaternion oldRotation;

    int viewId = 0;

    float updateRate = 0.3f;

    public void Start(){
		if (sceneView) {
			AltimitViewHandler.RegisterNetworkObject (this.gameObject, viewID);
		}

        InvokeRepeating("UpdateObject", 0f, updateRate);
	}

    public void UpdateObject()
    {
        if (isMine)
        {
            if (trackState != TrackState.none)
            {
                currentTime = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;

                if (gameObject.transform.position != oldPosition || gameObject.transform.rotation != oldRotation && trackState == TrackState.positionRotation)
                {
                    AltimitNetwork.Send("UpdateUnityObject", gameObject.transform.position, gameObject.transform.rotation, (long)currentTime, viewID);
                    oldPosition = gameObject.transform.position;
                    oldRotation = gameObject.transform.rotation;
                }
                else if (gameObject.transform.position != oldPosition && trackState == TrackState.position)
                {
                    AltimitNetwork.Send("UpdateUnityObject", gameObject.transform.position, (long)currentTime, viewID);
                    oldPosition = gameObject.transform.position;
                }
                else if (gameObject.transform.rotation != oldRotation && trackState == TrackState.rotation)
                {
                    AltimitNetwork.Send("UpdateUnityObject", gameObject.transform.rotation, (long) currentTime, viewID);
                    oldRotation = gameObject.transform.rotation;
                }
            }
        }
    }

    public void UpdatePositionRotation(Vector3 position, Quaternion rotation, long timeStamp)
    {
        if(timeStamp > lastUpdate)
        {
            gameObject.transform.position = position;
            gameObject.transform.rotation = rotation;
            lastUpdate = timeStamp;
        }
    }

    public void UpdatePositionRotation(Vector3 position, long timeStamp)
    {
        if (timeStamp > lastUpdate)
        {
            gameObject.transform.position = position;
            lastUpdate = timeStamp;
        }
    }

    public void UpdatePositionRotation(Quaternion rotation, long timeStamp)
    {
        if (timeStamp > lastUpdate)
        {
            gameObject.transform.rotation = rotation;
            lastUpdate = timeStamp;
        }
    }

	public int viewID {
		get{
			return viewId;
		}
		set {
			if (viewID == 0) {
				viewId = value;
			}
		}
	}

	public bool isMine {
		get {
			return this.ownerID == AltimitPlayer.ID;
		}
	}
}
