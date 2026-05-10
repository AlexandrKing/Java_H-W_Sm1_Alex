package com.example.todolist.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that the due date is not before the creation timestamp.
 */
@Documented
@Constraint(validatedBy = DueDateNotBeforeCreationValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface DueDateNotBeforeCreation {
    String message() default "dueDate cannot be before task creation";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
