package com.taskManager.model;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Task {

    @Id
    @GeneratedValue 
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String description;
    @Column(nullable = false)
    @Value("false")
    private boolean completed;
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Task() {
        // Default constructor for JPA
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    
}
