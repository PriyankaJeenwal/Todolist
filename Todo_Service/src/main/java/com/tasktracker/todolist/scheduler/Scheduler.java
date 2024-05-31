package com.tasktracker.todolist.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tasktracker.todolist.entity.Task;
import com.tasktracker.todolist.repository.TaskRepository;

@Component
public class Scheduler {

	@Autowired
	TaskRepository taskRepository;

	@Scheduled(fixedRate = 60000)
	public void scheduleNotification() {
		System.out.println("scheduler");
		LocalDateTime currentTime = LocalDateTime.now();

		List<Task> taskList = taskRepository.findAll();
		for (Task task : taskList) {

			System.out.println(currentTime);
			LocalDateTime complitiontime = task.getCompletionDate();
			System.out.println(complitiontime);
			System.out.println(task.getCompletionDate());

			if (currentTime.withSecond(0).withNano(0).equals(complitiontime.minusHours(1).withSecond(0).withNano(0))) {
				System.out.println("your task" + task.getTitle() + " is near complition date");
			}
		}
	}

}
