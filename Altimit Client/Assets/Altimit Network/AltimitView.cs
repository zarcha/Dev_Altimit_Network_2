using UnityEngine;
using Altimit;
using System;
using System.Collections;

public class AltimitView : MonoBehaviour {

    public TrackState trackState;

	public int subId;
	public int ownerID;

    public string ownerName;

	public bool sceneView = false;

    long lastUpdate = 0L;
    long currentTime = 0L;

    Vector3 oldPosition;
    Quaternion oldRotation;

    int viewId = 0;

    float updateRate = 0.3f;

    public void Start(){
		if (sceneView) {
            AltimitViewHandler.RegisterNetworkObject(this.gameObject, viewID);
		}

        InvokeRepeating("UpdateObject", 0f, updateRate);
	}

    public void UpdateObject()
    {
        if (isMine || (sceneView && AltimitRoom.RoomOwner))
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

    private IEnumerator MoveToPosition(Vector3 position, Quaternion rotation, float timeToMove)
    {
        Vector3 currentPos = transform.position;
        Quaternion currentRot = transform.rotation;
        float t = 0f;
        while (t < 1)
        {
            t += Time.deltaTime / timeToMove;
            transform.position = Vector3.Lerp(currentPos, position, t);
            transform.rotation = Quaternion.Lerp(currentRot, rotation, t);
            yield return null;
        }
    }

    public void UpdatePositionRotation(Vector3 position, Quaternion rotation, long timeStamp)
    {
        if (timeStamp > lastUpdate && trackState != TrackState.none)
        {
            //gameObject.transform.position = position;
            StartCoroutine(MoveToPosition(position, rotation, updateRate));
            //gameObject.transform.rotation = rotation;
            lastUpdate = timeStamp;
        }
    }

    public void UpdatePositionRotation(Vector3 position, long timeStamp)
    {
        if (timeStamp > lastUpdate && trackState != TrackState.none)
        {

            gameObject.transform.position = position;
            lastUpdate = timeStamp;
        }
    }

    public void UpdatePositionRotation(Quaternion rotation, long timeStamp)
    {
        if (timeStamp > lastUpdate && trackState != TrackState.none)
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
