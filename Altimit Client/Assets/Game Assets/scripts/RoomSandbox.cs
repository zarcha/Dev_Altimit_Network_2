using UnityEngine;
using System.Collections;
using Altimit;
using UnityEngine.UI;

public class RoomSandbox: MonoBehaviour {

    public GameObject roomLabel;
    public GameObject userCountLabel;
    public GameObject newRoomTextbox;
    public GameObject usernameTextbox;
    public GameObject cube;
    public GameObject canvas;

    private Camera mainCamera;

	void Update () {
        roomLabel.GetComponent<Text>().text = "Current Room: " + AltimitRoom.RoomName;
        userCountLabel.GetComponent<Text>().text = "User Count: " + AltimitRoom.UsersInRoom;

        mainCamera = gameObject.GetComponent<Camera>();
    }

    public void JoinRoom()
    {
        AltimitRoom.JoinRoom(newRoomTextbox.GetComponent<InputField>().text);
    }

	public void InstantiateCube(){
       
        AltimitNetwork.Instantiate("FPSController", new Vector3(0, 2, 0), Quaternion.identity);

        mainCamera.enabled = false;
        canvas.SetActive(false);
	}

    public void setUsername()
    {
        AltimitPlayer.Username = usernameTextbox.GetComponent<InputField>().text;
    }
}
