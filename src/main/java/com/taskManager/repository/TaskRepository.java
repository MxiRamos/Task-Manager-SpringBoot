package com.taskManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskManager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
