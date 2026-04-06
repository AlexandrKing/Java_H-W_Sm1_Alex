package com.example.todolist.service;

import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service for demonstrating @Qualifier usage.
 * Injects both primary and stub repositories.
 */
@Service
public class TaskStatisticsService {

    private final TaskRepository primaryRepository;
    private final TaskRepository stubRepository;

    /**
     * Constructor with qualified injections.
     * @param primaryRepository the primary repository (InMemoryTaskRepository)
     * @param stubRepository the stub repository (StubTaskRepository)
     */
    @Autowired
    public TaskStatisticsService(TaskRepository primaryRepository,
                                 @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
        this.primaryRepository = primaryRepository;
        this.stubRepository = stubRepository;
    }

    /**
     * Compares the number of tasks in both repositories.
     * @return a string describing the comparison
     */
    public String compareRepositories() {
        int primaryCount = primaryRepository.findAll().size();
        int stubCount = stubRepository.findAll().size();
        return String.format("Primary repository has %d tasks, Stub repository has %d tasks.",
                primaryCount, stubCount);
    }
}