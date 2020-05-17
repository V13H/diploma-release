package com.hvs.diploma.services;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.AccountSettings;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.repositories.AccountRepository;
import com.hvs.diploma.repositories.AccountSettingsRepository;
import com.hvs.diploma.repositories.TaskRepository;
import com.hvs.diploma.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MainService {
    private static final Timestamp[] DEFAULT_DATE_FILTER_PARAMS = new Timestamp[]{DateHelper.today(), DateHelper.maxDate()};
    private static final TaskStatus[] DEFAULT_STATUS_FILTER_PARAMS = new TaskStatus[]{TaskStatus.ACTIVE, TaskStatus.EXPIRED};
    private static final TaskPriority[] DEFAULT_PRIORITY_FILTER_PARAMS = new TaskPriority[]
            {TaskPriority.HIGH, TaskPriority.MEDIUM, TaskPriority.LOW};

    private final AccountRepository accountRepository;
    private final TaskRepository taskRepository;
    private final AccountSettingsRepository accountSettingsRepository;


    @Autowired
    public MainService(AccountRepository accountRepository, TaskRepository taskRepository, AccountSettingsRepository accountSettingsRepository) {
        this.accountRepository = accountRepository;
        this.taskRepository = taskRepository;
        this.accountSettingsRepository = accountSettingsRepository;
    }

    @Transactional(readOnly = true)
    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account findAccountBySocialId(String socialId) {
        return accountRepository.findAccountBySocialId(socialId);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasksForAccount(Account account, Pageable pageable) {
        return taskRepository.findAllByOwner(account, pageable);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllUndoneTasksForAccount(Account account, Pageable pageable) {
        return taskRepository.findAllByOwnerAndStatusIsNot(account, TaskStatus.DONE, pageable);
    }

    @Transactional(readOnly = true)
    public long countTasksByStatusIsNot(Account account, TaskStatus status) {
        return taskRepository.countTasksByOwnerAndStatusIsNot(account, status);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByFilterParameters(Account owner, Pageable pageable,
                                                 TaskPriority[] priorities,
                                                 TaskStatus[] statuses,
                                                 Timestamp[] dates) {
        if (priorities == null) {
            priorities = DEFAULT_PRIORITY_FILTER_PARAMS;
        }
        if (statuses == null) {
            statuses = DEFAULT_STATUS_FILTER_PARAMS;
        }
        if (dates == null) {
            dates = DEFAULT_DATE_FILTER_PARAMS;
        }
        return taskRepository.findTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities, statuses, dates, pageable);
    }

    @Transactional(readOnly = true)
    public List<Task> filteredList(Account owner, TaskPriority[] priorities, TaskStatus[] statuses,
                                   Timestamp[] dates) {
        if (priorities == null) {
            priorities = DEFAULT_PRIORITY_FILTER_PARAMS;
        }
        if (statuses == null) {
            statuses = DEFAULT_STATUS_FILTER_PARAMS;
        }
        return taskRepository.findTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities, statuses, dates);
    }

    @Transactional(readOnly = true)
    public long countTasksByFilterParams(Account owner, TaskPriority[] priorities, TaskStatus[] statuses, Timestamp[] dates) {
        if (priorities == null) {
            priorities = DEFAULT_PRIORITY_FILTER_PARAMS;
        }
        if (statuses == null) {
            statuses = DEFAULT_STATUS_FILTER_PARAMS;
        }
        if (dates == null) {
            dates = DEFAULT_DATE_FILTER_PARAMS;
        }
        return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities, statuses, dates);
    }

    @Transactional(readOnly = true)
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public long countTasksByOwner(Account owner) {
        return taskRepository.countTasksByOwner(owner);
    }

    @Transactional
    public void deleteTaskById(Long id) {
        taskRepository.deleteTaskById(id);
    }

    @Transactional
    public void markTaskAsDoneById(Long id) {
        Task tasksById = taskRepository.findTasksById(id);
        tasksById.setStatus(TaskStatus.DONE);
    }

    @Transactional
    public void saveSettings(AccountSettings accountSettings) {
        accountSettingsRepository.save(accountSettings);
    }
}
