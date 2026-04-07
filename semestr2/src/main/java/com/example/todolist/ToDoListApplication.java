package com.example.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for the To-Do List Manager.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
public class ToDoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToDoListApplication.class, args);
    }
}