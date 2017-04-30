using UnityEngine;
using System.Collections;
using UnityEditor;
using Altimit;

[CustomEditor(typeof(AltimitView))]
[CanEditMultipleObjects]
public class AltimitViewEditor : Editor {
	public override void OnInspectorGUI() {
		AltimitView view = (AltimitView)target;

		if (view.viewID == 0) {
			view.viewID = GameObject.FindObjectsOfType<AltimitView> ().Length;
			view.sceneView = true;
		}

		EditorGUILayout.LabelField ("View ID: ", view.viewID.ToString());
		EditorGUILayout.LabelField ("Owner ID: ", view.ownerID.ToString ());
		EditorGUILayout.LabelField ("Scene View: ", view.sceneView.ToString ());
		view.trackState = (TrackState)EditorGUILayout.EnumPopup ("Track State", view.trackState);
	}
}
