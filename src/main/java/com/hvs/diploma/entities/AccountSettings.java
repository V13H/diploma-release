package com.hvs.diploma.entities;

import com.hvs.diploma.enums.TaskPriority;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Getter
@Setter
public class AccountSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sortBy;
    private String sortOrder;
    @ElementCollection
    private List<TaskPriority> priorityFilterParams;
    @ElementCollection
    private List<TaskPriority> statusFilterParams;
    @ElementCollection
    private List<Timestamp> deadlineFilterParams;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account", nullable = false)
    private Account account;

    public AccountSettings(String sortBy, String sortOrder) {
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public AccountSettings() {
    }
}
