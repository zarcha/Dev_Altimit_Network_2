using UnityEngine;
using System.Collections.Generic;
//using UnityEditor.VersionControl;
using System.Threading;

public class TaskContainer {

	static TaskContainer container = null;
	public Queue<Task> TaskQueue = new Queue<Task>();
	public object _queueLock = new object();

	public TaskContainer(){
		TaskQueue = new Queue<Task> ();
		_queueLock = new object ();
	}

	public void ScheduleTask(Task newTask){
		try{
			lock (_queueLock) {
				if(TaskQueue.Count < 1000)
				{
					TaskQueue.Enqueue(newTask);
				}
			}
		}catch(ThreadInterruptedException e){
			Debug.Log(e);
		}
	} 

	public static TaskContainer GetInstance(){

		if (container == null) {
			container = new TaskContainer();
		}

		return container;
	}
}
