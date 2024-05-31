package com.tasktracker.userservice.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tasktracker.userservice.Entity.TaskResponse;
import com.tasktracker.userservice.Entity.Task;

@FeignClient(url = "http://localhost:8084", value = "TaskClient")
public interface TaskClient {

//	@GetMapping("/task/user/{userId}")
//	List<Task> getTasksOfUser(@PathVariable Long userId);
//	
	@GetMapping("/task/user/{userId}")
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByUserId(@PathVariable Long userId);

}
