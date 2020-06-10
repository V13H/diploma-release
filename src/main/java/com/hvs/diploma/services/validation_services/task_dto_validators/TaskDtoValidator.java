package com.hvs.diploma.services.validation_services.task_dto_validators;

import com.hvs.diploma.dto.TaskDTO;
import org.springframework.validation.Validator;

public abstract class TaskDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TaskDTO.class);
    }
}
