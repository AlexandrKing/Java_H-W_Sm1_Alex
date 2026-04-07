package com.example.todolist.validation;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.dto.TaskUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Validator for DueDateNotBeforeCreation.
 */
@Component
public class DueDateNotBeforeCreationValidator implements ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {

    private final TaskRepository taskRepository;

    @Autowired
    public DueDateNotBeforeCreationValidator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getDueDate() == null) {
            return true;
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return true;
        }

        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (request == null) {
            return true;
        }

        String uri = request.getRequestURI();
        String[] parts = uri.split("/");
        if (parts.length == 0) {
            return true;
        }

        String lastPart = parts[parts.length - 1];
        try {
            Long taskId = Long.parseLong(lastPart);
            Optional<Task> taskOptional = taskRepository.findById(taskId);
            if (!taskOptional.isPresent()) {
                return true;
            }
            Task existing = taskOptional.get();
            return !dto.getDueDate().isBefore(existing.getCreatedAt().toLocalDate());
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
