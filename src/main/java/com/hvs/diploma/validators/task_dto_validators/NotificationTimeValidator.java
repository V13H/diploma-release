package com.hvs.diploma.validators.task_dto_validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.enums.ErrorCode;
import com.hvs.diploma.util.DateTimeHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.text.ParseException;
import java.time.LocalTime;

@Service
public class NotificationTimeValidator extends TaskDtoValidator {
    private static final String FIELD_NAME = "notificationTime";
    private static final String INVALID_VALUE = ErrorCode.INVALID_TIME_VALUE.getValue();
    private static final String INVALID_FORMAT = ErrorCode.INVALID_TIME_FORMAT.getValue();

    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO dto = (TaskDTO) o;
        String notificationTime = dto.getNotificationTime();
        LocalTime time;
        if (!notificationTime.isEmpty()) {
            try {
                time = DateTimeHelper.parseTime(notificationTime);
                if (time.isBefore(LocalTime.now())) {
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
