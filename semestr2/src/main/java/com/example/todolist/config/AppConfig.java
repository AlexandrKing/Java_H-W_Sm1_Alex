package com.example.todolist.config;

import com.example.todolist.repository.StubTaskRepository;
import com.example.todolist.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for defining beans.
 * Configures the StubTaskRepository as a bean and enables JPA auditing.
 */
@Configuration
@EnableJpaAuditing
public class AppConfig {

    /**
     * Defines the StubTaskRepository bean.
     * Note: The StubTaskRepository is already annotated with @Repository,
     * but this demonstrates configuring it via @Bean.
     * @return an instance of StubTaskRepository
     */
    @Bean
    public TaskRepository stubTaskRepository() {
        return new StubTaskRepository();
    }
}