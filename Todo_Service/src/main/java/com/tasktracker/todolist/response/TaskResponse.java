package com.tasktracker.todolist.response;

import java.util.List;

import com.tasktracker.todolist.entity.Task;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskResponse<T> {

	private Object data;

	private String message;

	private boolean status;

	private boolean success;
	

	
	public TaskResponse(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
		
		}


	public TaskResponse(String tasksFetched, List<Task> tasks, boolean b) {
	    this.data=tasks;
	    this.message=tasksFetched;
	    this.status=b;
	}
	
	
	

}
