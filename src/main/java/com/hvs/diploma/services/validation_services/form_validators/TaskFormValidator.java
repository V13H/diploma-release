package com.hvs.diploma.services.validation_services.form_validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.services.validation_services.task_dto_validators.DeadlineValidator;
import com.hvs.diploma.services.validation_services.task_dto_validators.TimeValidator;
import com.hvs.diploma.util.ValidatorHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class TaskFormValidator implements Validator {
    private final TimeValidator timeValidator;
    private final DeadlineValidator deadlineValidator;

    @Autowired
    public TaskFormValidator(TimeValidator timeValidator, DeadlineValidator deadlineValidator) {
        this.timeValidator = timeValidator;
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
        timeValidator.validate(task, errors);
    }
}
