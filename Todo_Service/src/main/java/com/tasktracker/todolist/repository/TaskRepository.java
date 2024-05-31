package com.tasktracker.todolist.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tasktracker.todolist.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	// native query
	@Query(value = "select *from task  where completion_date >=:presentDate", nativeQuery = true)
	List<Task> getAllRemainingTask(@Param("presentDate") LocalDate presentDate);

	public List<Task> findTaskByCreationDate(LocalDateTime creationDate);

	public List<Task> findTaskByCompletionDate(LocalDateTime completionDate);

	public List<Task> findByuserId(Long userId);

	public Optional<Task> findTaskByTitleAndUserId(String title, Long id);

	public List<Task> findByCompletionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	// named query method
	public List<Task> findIncomplteTask();

}
