package com.tasktracker.todolist.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tasktracker.todolist.entity.Task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class TaskDao {

	@Autowired
	EntityManager em;

	public List<Task> findTaskByTitle(String title) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Task> cq = cb.createQuery(Task.class);

		Root<Task> task = cq.from(Task.class);
		Predicate titlePredicate = cb.equal(task.get("title"), title);
		cq.where(titlePredicate);
		TypedQuery<Task> query = em.createQuery(cq);
		return query.getResultList();
	}
}
