package com.hvs.diploma.dto;

import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.util.DateTimeHelper;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
public class TaskDTO {
    private String description;
    private String priority;
    private String deadline;
    private long id;
    private String notificationTime;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @SneakyThrows
    public Task buildTaskInstance() {
        Task task = new Task();
        task.setTaskDescription(description);
        task.setDeadline(DateTimeHelper.simpleDateFormat().parse(deadline));
        if (fieldNotNull(priority)) {
            task.setPriority(TaskPriority.valueOf(priority));
            task.setPriorityValue(TaskPriority.valueOf(priority).getPriorityValue());
        }
        if (fieldNotNull(notificationTime) && !notificationTime.isEmpty()) {
            task.setNotificationDate(DateTimeHelper.buildTimestampInstance(deadline, notificationTime));
        }
        logger.warn(task.toString());
        return task;
    }

    @SneakyThrows
    public Date getDeadlineDate() {
        return DateTimeHelper.simpleDateFormat().parse(deadline);
    }

    private boolean fieldNotNull(Object field) {
        return field != null;
    }

    @SneakyThrows
    public Timestamp getNotificationDate() {
        return DateTimeHelper.buildTimestampInstance(deadline, notificationTime);
    }

}
