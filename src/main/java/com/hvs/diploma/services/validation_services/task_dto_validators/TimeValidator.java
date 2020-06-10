package com.hvs.diploma.services.validation_services.task_dto_validators;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.enums.ErrorCode;
import com.hvs.diploma.util.DateTimeHelper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeValidator extends TaskDtoValidator {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TimeValidator.class);
    private static final String FIELD_NAME = "notificationTime";
    private static final String INVALID_VALUE = ErrorCode.INVALID_TIME_VALUE.getValue();
    private static final String INVALID_FORMAT = ErrorCode.INVALID_TIME_FORMAT.getValue();

    @Override
    public void validate(Object o, Errors errors) {
        TaskDTO dto = (TaskDTO) o;
        String notificationTime = dto.getNotificationTime();
        LocalTime time;
        if (notificationTime != null && !notificationTime.isEmpty()) {
            try {
                time = DateTimeHelper.parseTime(notificationTime);
                LocalDate deadline = DateTimeHelper.parseDate(dto.getDeadline());
                logger.warn(time.toString());
                if (time.isBefore(LocalTime.now()) && deadline.isEqual(LocalDate.now())) {
                    String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                    dto.setNotificationTime(timeNow);
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
