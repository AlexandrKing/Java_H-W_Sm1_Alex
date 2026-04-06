package com.example.todolist.config;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

/**
 * Bean with request scope.
 * Demonstrates that a new instance is created for each HTTP request.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean {

    private final String requestId;
    private final long startTime;

    public RequestScopedBean() {
        this.requestId = UUID.randomUUID().toString();
        this.startTime = System.currentTimeMillis();
    }

    public String getRequestId() {
        return requestId;
    }

    public long getStartTime() {
        return startTime;
    }
}