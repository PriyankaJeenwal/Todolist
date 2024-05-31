package com.tasktracker.todolist.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.response.TaskResponse;
import com.tasktracker.todolist.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskControllerImpl implements TaskController {

	@Autowired
	private TaskService taskService;

	static final Logger log = LogManager.getLogger(TaskControllerImpl.class.getName());

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByUserId(Long userId) {
		try {
			TaskResponse<List<Task>> taskResponse = taskService.getByUserId(userId);
			if (taskResponse.isStatus())
				return new ResponseEntity<TaskResponse<List<Task>>>(taskResponse, HttpStatus.OK);
			else
				return new ResponseEntity<TaskResponse<List<Task>>>(taskResponse, HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			log.info("exception : {}", e);
		}

		return new ResponseEntity<TaskResponse<List<Task>>>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<TaskResponse<Task>> addTask(Task task) {
		try {
			if (task.getTitle() == null || task.getDescription() == null || task.getCompletionDate() == null
					|| task.getUserId() == null) {
				return new ResponseEntity<>(
						new TaskResponse<Task>("Task not added. Required fields are missing.", false),
						HttpStatus.BAD_REQUEST);
			}
			log.info("adding new task : {}", task);

			TaskResponse<Task> taskResponse = taskService.addTask(task);

			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			log.error("Exception while adding task: {}", e.getMessage());
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> findAllTask(Integer pageSize, Integer page) {
		try {
			log.info("fetching all tasks");
			Pageable paging = PageRequest.of(page, pageSize);
			TaskResponse<List<Task>> taskResponse = taskService.getAllTask(paging);
			log.info("taskList :{} ", taskResponse.getData());
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<TaskResponse<Task>> getTaskById(Integer id) {
		try {
			if (id != 0 && id != null) {
				log.info("get Task By Id : {}", id);
				TaskResponse<Task> taskResponse = taskService.getTaskById(id);
				log.info("task :{}", taskResponse.getData());
				if (taskResponse.isStatus()) {
					return new ResponseEntity<>(taskResponse, HttpStatus.FOUND);
				} else {
					return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<TaskResponse<Task>>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByTitle(String title) {

		try {
			TaskResponse<List<Task>> taskResponse = taskService.getTaskByTitle(title);
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByCompletionDate(LocalDateTime completionDate) {
		try {
			TaskResponse<List<Task>> taskResponse = taskService.getTaskByCompletionDate(completionDate);
			log.info("taskList:{} ", taskResponse.getData());
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByCreationDate(LocalDateTime creationDate) {
		try {
			TaskResponse<List<Task>> taskResponse = taskService.getTaskByCreationDate(creationDate);
			log.info("taskList :{} ", taskResponse.getData());
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> getRemainingTask() {
		try {
			TaskResponse<List<Task>> taskResponse = taskService.getAllRemainingTask();
			log.info("taskList :{}", taskResponse.getData());
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<TaskResponse<List<Task>>> getIncompleteTask() {

		try {
			TaskResponse<List<Task>> taskResponse = taskService.getIncompleteTask();
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> deleteTaskById(Integer id) {
		try {
			log.info("deleting task by id : {} ", id);
			if (taskService.deleteTaskById(id)) {
				return new ResponseEntity<String>("Task deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Task not with given id not exist ", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
		}
		return new ResponseEntity<String>("Task  not exist  with given id", HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<TaskResponse<Task>> updateTask(Task task, Integer id) {
		try {

			log.info("updating the task :{}", task);
			TaskResponse<Task> taskResponse = taskService.getTaskById(id);
			if (taskResponse.isStatus()) {
				return new ResponseEntity<>(taskResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(taskResponse, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.info("exception : {}", e);
			return new ResponseEntity<>(new TaskResponse<>("Task not added. Internal Server Error", false),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
