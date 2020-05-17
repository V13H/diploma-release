package com.hvs.diploma.validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.util.DateHelper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class AddTaskValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(TaskDTO.class);
    }

    @SneakyThrows
    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO task = (TaskDTO) o;
        if (task.getDescription().isEmpty()) {
            errors.rejectValue("description", "value.empty");
        }
        if (task.getDeadline().isEmpty()) {
            errors.rejectValue("deadline", "value.empty");
        } else if (task.getDeadlineDate().before(DateHelper.today())) {
            errors.rejectValue("deadline", "value.date.invalid");
        }
    }
}
