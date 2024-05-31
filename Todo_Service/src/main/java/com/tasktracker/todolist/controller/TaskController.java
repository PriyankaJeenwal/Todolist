package com.tasktracker.todolist.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.response.TaskResponse;


public interface TaskController {

	@GetMapping("/user/{userId}")
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByUserId(@PathVariable Long userId);

	@PostMapping("/addTask")
	public ResponseEntity<TaskResponse<Task>> addTask(@RequestBody Task task);

	@GetMapping("/getAll")
	public ResponseEntity<TaskResponse<List<Task>>> findAllTask(@RequestParam(defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(defaultValue = "0", required = false) Integer page);

	@GetMapping("getTaskById/{id}")
	public ResponseEntity<TaskResponse<Task>> getTaskById(@PathVariable Integer id);

	@GetMapping("/getByTitle/{title}")
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByTitle(@PathVariable String title);

	@GetMapping("/completionDate")
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByCompletionDate(
			@RequestParam("completionDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime completionDate);

	@GetMapping("/creationDate")
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByCreationDate(
			@RequestParam("creationDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime creationDate);

	@GetMapping("/remainingTask")
	public ResponseEntity<TaskResponse<List<Task>>> getRemainingTask();

	@GetMapping("/incompleteTask")
	public ResponseEntity<TaskResponse<List<Task>>> getIncompleteTask();

	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteTaskById(@PathVariable Integer id);

	@PostMapping("/update/{id}")
	public ResponseEntity<TaskResponse<Task>> updateTask(@RequestBody Task task, @PathVariable Integer id);
	
	
	
	
	
	
	
	
	
	
	
	

}
