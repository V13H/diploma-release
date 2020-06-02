package com.hvs.diploma.validators.task_dto_validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.enums.ErrorCode;
import com.hvs.diploma.util.DateTimeHelper;
import com.hvs.diploma.util.ValidatorHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.text.ParseException;
import java.time.LocalDate;

@Service
public class DeadlineValidator extends TaskDtoValidator {
    private static final String FIELD_NAME = "deadline";
    private static final String INVALID_VALUE = ErrorCode.INVALID_DEADLINE_DATE.getValue();
    private static final String INVALID_FORMAT = ErrorCode.INVALID_DEADLINE_FORMAT.getValue();

    @Override
    public void validate(Object target, Errors errors) {
        TaskDTO task = (TaskDTO) target;
        if (!ValidatorHelper.isRequiredFieldEmpty(task.getDeadline(), FIELD_NAME, errors)) {
            LocalDate deadlineDate;

            try {
                deadlineDate = DateTimeHelper.parseDate(task.getDeadline());
                if (deadlineDate.isBefore(LocalDate.now())) {
                    errors.rejectValue(FIELD_NAME, INVALID_VALUE);
                }
            } catch (IllegalArgumentException | ParseException exception) {
                if (exception instanceof ParseException) {

                    errors.rejectValue(FIELD_NAME, INVALID_FORMAT);
                } else {
                    errors.rejectValue(FIELD_NAME, INVALID_VALUE);
                }
            }
        }
    }
}
