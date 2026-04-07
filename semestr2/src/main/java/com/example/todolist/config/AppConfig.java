package com.example.todolist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for defining beans.
 * Enables JPA auditing.
 */
@Configuration
@EnableJpaAuditing
public class AppConfig {
}