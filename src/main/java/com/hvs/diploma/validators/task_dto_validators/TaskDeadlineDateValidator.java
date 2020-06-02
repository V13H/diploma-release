package com.hvs.diploma.validators.task_dto_validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.util.DateTimeHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.text.ParseException;

@Service
public class TaskDeadlineDateValidator extends TaskDtoValidator {
    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO taskDTO = (TaskDTO) o;
        String deadline = taskDTO.getDeadline();
        try {
            DateTimeHelper.simpleDateFormat().parse(deadline);
        } catch (ParseException e) {
            errors.rejectValue("deadline", "deadline.invalid-value");
        }
    }

    private boolean matchesToValidPattern(String s) {
        return s.matches("^[a-zA-Z]{3}[ ]");
    }
}
