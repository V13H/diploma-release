package com.hvs.diploma.entities;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String taskDescription;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Temporal(TemporalType.DATE)
    private Date deadline;
    private Timestamp notificationDate;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    private int priorityValue;
    @Enumerated(EnumType.ORDINAL)
    private TaskStatus status = TaskStatus.ACTIVE;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account owner;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", deadline=" + deadline +
                ", priority=" + priority +
                ", priorityValue=" + priorityValue +
                ", status=" + status +
                '}';
    }

    public TaskDTO toDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setPriority(this.priority.toString());
        taskDTO.setDeadline(this.deadline.toString());
        taskDTO.setDescription(this.taskDescription);
        taskDTO.setId(this.id);
        return taskDTO;
    }
}
