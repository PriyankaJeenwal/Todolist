package com.tasktracker.todolist.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.repository.TaskDao;
import com.tasktracker.todolist.repository.TaskRepository;
import com.tasktracker.todolist.response.TaskResponse;
import com.tasktracker.todolist.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	TaskDao taskDao;

	private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Override
	public TaskResponse<List<Task>> getByUserId(Long userId) {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<List<Task>>();
		try {
			List<Task> taskList = taskRepository.findByuserId(userId);
			if (!taskList.isEmpty()) {

				taskResponse.setData(
						taskList.stream().sorted((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()))
								.collect(Collectors.toList()));
				taskResponse.setMessage("success");
				taskResponse.setStatus(true);
				return taskResponse;
			}
		} catch (Exception e) {
			log.error("exception " + e.toString());
		}
		taskResponse.setMessage("no data found");
		taskResponse.setStatus(false);
		return taskResponse;

	}

	@Override
	public TaskResponse<Task> addTask(Task task) {
		TaskResponse<Task> taskResponse = new TaskResponse<Task>();
		log.info("task :{}", task);
		try {
			if (task.getTitle() != null && task.getUserId() != null) {
				Optional<Task> existingTask = taskRepository.findTaskByTitleAndUserId(task.getTitle().toLowerCase(),
						task.getUserId());

				log.info("existingTask :{}", existingTask);
				if (existingTask.isEmpty()) {
					log.info("existingTask is empty we can add new task");
					Task newTask = new Task();

					newTask.setTitle(task.getTitle().toLowerCase());
					newTask.setUserId(task.getUserId());
					newTask.setDescription(task.getDescription());
					newTask.setCompletionDate(task.getCompletionDate());
					newTask.setPriority(task.getPriority());
					newTask.setStatus(task.getStatus());
					newTask.setRating(task.getRating());
					newTask.setTodoType(task.getTodoType());
					newTask.setTags(task.getTags());
					taskRepository.save(newTask);
					List<Task> taskList = new ArrayList<Task>();
					taskList.add(newTask);
					taskResponse.setData(taskList);
					taskResponse.setMessage("success");
					taskResponse.setStatus(true);
					return taskResponse;
				}
			}

		} catch (Exception e) {
			log.error("exception in addTask : {}", e);
		}
		log.info("task already exist");
		taskResponse.setMessage("task not saved");
		taskResponse.setStatus(false);
		return taskResponse;
	}

	@Override
	public TaskResponse<List<Task>> getAllTask(Pageable paging) {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
		try {
			Page<Task> taskList = taskRepository.findAll(paging);
			List<Task> list = taskList.getContent();
			log.info("list of tasks:{} ", taskList);
			if (!list.isEmpty()) {
				log.info("taskList is not empty");
				taskResponse.setData(
						list.stream().sorted((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()))
								.collect(Collectors.toList()));
				taskResponse.setMessage("success");
				taskResponse.setStatus(true);
				return taskResponse;
			}

		} catch (Exception e) {
			log.error("exception in getAllTask :{} ", e);
		}

		taskResponse.setMessage("task not saved");
		taskResponse.setStatus(false);
		return taskResponse;

	}

	@Override
	public TaskResponse<Task> getTaskById(Integer id) {
		log.info("Id : {}", id);
		TaskResponse<Task> taskResponse = new TaskResponse<Task>();
		try {
			Optional<Task> task = taskRepository.findById(id);
			if (!task.isEmpty()) {
				Task taskObj = task.get();
				log.info("task is not empty");
				List<Task> taskList = new ArrayList<Task>();
				taskList.add(taskObj);
				taskResponse.setData(taskList);
				taskResponse.setMessage("task found");
				taskResponse.setStatus(true);
				return taskResponse;
			}
		} catch (Exception e) {
			log.error("exception in getTaskById : {}", e);
		}

		taskResponse.setMessage("task not found");
		taskResponse.setStatus(false);
		return taskResponse;
	}

	@Override
	public TaskResponse<List<Task>> getTaskByTitle(String title) {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
		try {
			List<Task> list = taskDao.findTaskByTitle(title);
			if (!list.isEmpty()) {
				taskResponse.setData(list);
				taskResponse.setMessage("task found");
				taskResponse.setStatus(true);
				return taskResponse;
			}

		} catch (Exception e) {
			log.error("exception {}", e);
		}
		taskResponse.setMessage("task not found");
		taskResponse.setStatus(false);
		return taskResponse;

	}

	@Override
	public TaskResponse<List<Task>> getTaskByCompletionDate(LocalDateTime completionDate) {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
		try {
			List<Task> task = taskRepository.findTaskByCompletionDate(completionDate);
			log.info("taskList:{} ", task);
			if (!task.isEmpty()) {
				log.info("taskList:{} ", task);
				taskResponse.setData(task);
				taskResponse.setMessage("task found");
				taskResponse.setStatus(true);
				return taskResponse;

			}
		} catch (Exception e) {
			log.error("exception in getTaskByCompletionDate:{} ", e);
		}
		log.info("task not found");
		taskResponse.setMessage("task not found");
		taskResponse.setStatus(false);
		return taskResponse;
	}

	@Override
	public TaskResponse<List<Task>> getTaskByCreationDate(LocalDateTime creationDate) {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
		try {
			List<Task> task = taskRepository.findTaskByCreationDate(creationDate);
			log.info("taskList :{}", task);
			if (!task.isEmpty()) {
				taskResponse.setData(task);
				taskResponse.setMessage("task found");
				taskResponse.setStatus(true);
				return taskResponse;
			}
		} catch (Exception e) {
			log.error("exception in getTaskByCreationDate : {}", e);
		}
		log.info("task not found");
		taskResponse.setMessage("task not found");
		taskResponse.setStatus(false);
		return taskResponse;
	}

	@Override
	public TaskResponse<List<Task>> getAllRemainingTask() {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<>();
		try {
			LocalDate date = LocalDate.now();
			List<Task> taskList = taskRepository.getAllRemainingTask(date);
			log.info("taskList " + taskList);
			if (!taskList.isEmpty()) {
				log.info("taskList is not empty :{}", taskList);
				taskResponse.setData(taskList);
				taskResponse.setMessage("task found");
				taskResponse.setStatus(true);
				return taskResponse;
			}

		} catch (Exception e) {
			log.error("exception in updateTask :{} ", e);
		}
		log.info("tasks not found");
		taskResponse.setMessage("task not found");
		taskResponse.setStatus(false);
		return taskResponse;
	}

	@Override
	public TaskResponse<List<Task>> getIncompleteTask() {
		TaskResponse<List<Task>> taskResponse = new TaskResponse<>();

		try {
			List<Task> taskList = taskRepository.findIncomplteTask();
			if (!taskList.isEmpty()) {
				taskResponse.setData(taskList);
				taskResponse.setMessage("task found");
				taskResponse.setStatus(true);
				return taskResponse;
			}

		} catch (Exception e) {
			log.error("exception {}", e);
		}

		taskResponse.setMessage("task not found");
		taskResponse.setStatus(false);
		return taskResponse;
	}

	@Override
	public boolean deleteTaskById(Integer id) {
		try {
			log.info("id : {}", id);
			Optional<Task> task = taskRepository.findById(id);
			if (task.isPresent()) {
				log.info("task is not empty ; {}", task);
				taskRepository.deleteById(id);
				return true;
			}
		} catch (Exception e) {
			log.error("exception :{} ", e);
		}
		log.info("task not found with id : {}", id);
		return false;

	}

	@Override
	public TaskResponse<Task> updateTask(Task newTask, Integer id) {
		TaskResponse<Task> taskResponse = new TaskResponse<Task>();
		try {
			if (id != null && newTask != null) {
				Optional<Task> task = taskRepository.findById(id);
				log.info("update task :{}", task);

				if (task.isPresent()) {
					log.info(" task is not null :{}", task);
					Task taskObj = task.get();
					List<Task> taskList = new ArrayList<Task>();
					if (newTask.getDescription() != null) {
						log.info(" description is not null :{}", newTask.getDescription());
						taskObj.setDescription(newTask.getDescription());
					}
					if (newTask.getCompletionDate() != null) {
						log.info(" completionDate is not null :{}", newTask.getCompletionDate());
						taskObj.getCompletionDateHistory().add(task.get().getCompletionDate());
						taskObj.setCompletionDate(newTask.getCompletionDate());
					}
					taskList.add(taskObj);
					taskResponse.setData(taskList);
					taskResponse.setMessage("task updated");
					taskResponse.setStatus(true);
					return taskResponse;

				}
			}

		} catch (Exception e) {
			log.error("exception in update task :{} ", e);
		}
		log.info("task not found with id : {}", id);
		taskResponse.setMessage("task not updated");
		taskResponse.setStatus(false);
		return taskResponse;

	}

}
