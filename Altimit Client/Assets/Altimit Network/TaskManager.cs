using UnityEngine;
using Altimit;

public delegate void Task();

public class TaskManager : MonoBehaviour {

	TaskContainer container = null;

	void Start()
	{
		container = TaskContainer.GetInstance ();
	}

	Task DequeueTask()
	{
		if (container == null) 
			return null;
		Task task = null;
		lock (container._queueLock) 
		{
			if (container.TaskQueue.Count > 0) {
				task = container.TaskQueue.Dequeue ();
			}
		}
		return task;
	}

	void Update () 
	{
		Task current = DequeueTask();
		float timeout = Time.realtimeSinceStartup + 0.04f;
		while(current != null)
		{
			current();
			if (Time.realtimeSinceStartup > timeout)
				break;
			current = DequeueTask();
		}
	}
}