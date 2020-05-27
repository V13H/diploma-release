package com.hvs.diploma.entities;

import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}
