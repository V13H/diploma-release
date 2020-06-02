package com.hvs.diploma.dto;

import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.util.DateTimeHelper;
import lombok.*;

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

    @SneakyThrows
    public Task buildTaskInstance() {
        Task task = new Task();
        task.setTaskDescription(description);
        task.setPriority(TaskPriority.valueOf(priority));
        task.setPriorityValue(TaskPriority.valueOf(priority).getPriorityValue());
        task.setDeadline(DateTimeHelper.simpleDateFormat().parse(deadline));
        return task;
    }

    @SneakyThrows
    public Date getDeadlineDate() {
        return DateTimeHelper.simpleDateFormat().parse(deadline);
    }

}
