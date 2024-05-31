package com.tasktracker.userservice.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tasktracker.userservice.enumdata.PriorityEnum;
import com.tasktracker.userservice.enumdata.RatingEnum;
import com.tasktracker.userservice.enumdata.StatusEnum;
import com.tasktracker.userservice.enumdata.TodoTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
	private Integer id;

	private String title;

	private String description;

	private LocalDate creationDate;

	private LocalDate completionDate;

	private PriorityEnum priority;

	private Integer userid;

	private StatusEnum status;

	private List<String> tags;

	private TodoTypeEnum todoType;

	private RatingEnum rating;

	List<LocalDate> completionDateHistory = new ArrayList<LocalDate>();

}
