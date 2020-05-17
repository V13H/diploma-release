package com.hvs.diploma.dto;

import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Getter
@Setter
@Data
@NoArgsConstructor
public class TaskDTO {
    private String description;
    private String priority;
    private String deadline;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    @SneakyThrows
    public Task buildTaskInstance() {
        Task task = new Task();
        task.setTaskDescription(description);
        task.setPriority(TaskPriority.valueOf(priority));
        task.setPriorityValue(TaskPriority.valueOf(priority).getPriorityValue());
        task.setDeadline(dateFormat.parse(deadline));
        return task;
    }

    @SneakyThrows
    public Date getDeadlineDate() {
        return dateFormat.parse(deadline);
    }
}
