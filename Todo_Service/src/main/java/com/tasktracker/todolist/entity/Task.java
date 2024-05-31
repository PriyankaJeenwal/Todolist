package com.tasktracker.todolist.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.tasktracker.todolist.enumdata.PriorityEnum;
import com.tasktracker.todolist.enumdata.RatingEnum;
import com.tasktracker.todolist.enumdata.StatusEnum;
import com.tasktracker.todolist.enumdata.TodoTypeEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Task.findIncomplteTask", query = "select t from Task t where t.status=0")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@CreationTimestamp
	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Column(name = "completion_date")
	private LocalDateTime completionDate;

	@Column(name = "priority")
	private PriorityEnum priority;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "status")
	private StatusEnum status;

	@Column(name = "todoType")
	private TodoTypeEnum todoType;

	@Column(name = "rating")
	private RatingEnum rating;

	@ElementCollection
	@CollectionTable(name = "tagList", joinColumns = @JoinColumn(name = "taskId"))
	private List<String> tags;

	@ElementCollection
	@CollectionTable(name = "CollectionHistory", joinColumns = @JoinColumn(name = "taskId"))
	private List<LocalDateTime> completionDateHistory;

}
