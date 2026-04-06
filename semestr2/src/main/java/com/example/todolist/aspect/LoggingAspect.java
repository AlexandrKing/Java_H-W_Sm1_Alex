package com.example.todolist.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Aspect for logging method executions in the service layer.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final String LOG_DIR = "service";
    private static final String LOG_FILE = LOG_DIR + "/service.log";

    public LoggingAspect() {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
    }

    @Around("execution(* com.example.todolist.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logToFile("Starting execution of " + methodName + " with args: " + java.util.Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logToFile("Completed execution of " + methodName + " in " + (endTime - startTime) + "ms, result: " + result);
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logToFile("Failed execution of " + methodName + " in " + (endTime - startTime) + "ms, error: " + throwable.getMessage());
            throw throwable;
        }
    }

    private void logToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(java.time.LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}