package com.tasktracker.userservice.Entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskResponse<T> {
	
	private List<Task> data;
	
	private String message;
	
	private boolean status;

	public TaskResponse(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
	}

	
	public TaskResponse(List<Task> data, String message, boolean status) {
		super();
		this.data = data;
		this.message = message;
		this.status = status;
	}


}
