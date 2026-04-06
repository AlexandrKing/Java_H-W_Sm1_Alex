package com.example.todolist.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor for logging the lifecycle of TaskService and TaskRepository beans.
 */
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof com.example.todolist.service.TaskService ||
            bean instanceof com.example.todolist.repository.TaskRepository) {
            System.out.println("Before initialization: " + beanName + " of type " + bean.getClass().getSimpleName());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof com.example.todolist.service.TaskService ||
            bean instanceof com.example.todolist.repository.TaskRepository) {
            System.out.println("After initialization: " + beanName + " of type " + bean.getClass().getSimpleName());
        }
        return bean;
    }
}