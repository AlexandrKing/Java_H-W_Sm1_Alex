package com.example.todolist.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Bean with prototype scope.
 * Demonstrates that a new instance is created for each injection.
 */
@Component
@Scope("prototype")
public class PrototypeScopedBean {

    private final String uniqueId;

    public PrototypeScopedBean() {
        this.uniqueId = UUID.randomUUID().toString();
    }

    public String generateTaskId() {
        return uniqueId;
    }
}