package com.hvs.diploma.validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.util.ValidatorHelper;
import com.hvs.diploma.validators.task_dto_validators.DeadlineValidator;
import com.hvs.diploma.validators.task_dto_validators.NotificationTimeValidator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class TaskFormValidator implements Validator {
    private final NotificationTimeValidator notificationTimeValidator;
    private final DeadlineValidator deadlineValidator;

    @Autowired
    public TaskFormValidator(NotificationTimeValidator notificationTimeValidator, DeadlineValidator deadlineValidator) {
        this.notificationTimeValidator = notificationTimeValidator;
        this.deadlineValidator = deadlineValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TaskDTO.class);
    }

    @SneakyThrows
    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO task = (TaskDTO) o;
        ValidatorHelper.isRequiredFieldEmpty(task.getDescription(), "description", errors);
        deadlineValidator.validate(task, errors);
        notificationTimeValidator.validate(task, errors);
    }
}
