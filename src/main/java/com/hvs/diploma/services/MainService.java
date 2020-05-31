package com.hvs.diploma.services;

import com.hvs.diploma.controllers.TasksController;
import com.hvs.diploma.dao.main.AccountRepository;
import com.hvs.diploma.dao.main.AccountSettingsRepository;
import com.hvs.diploma.dao.main.TaskRepository;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.AccountSettings;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.util.DateHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MainService {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TasksController.class);
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
                                                 List<TaskPriority> priorities,
                                                 List<TaskStatus> statuses,
                                                 List<TaskDeadlines> dates) {
        if (!parametersExists(priorities)) {
            priorities = new ArrayList<>();
            Collections.addAll(priorities, DEFAULT_PRIORITY_FILTER_PARAMS);
        }
        if (!parametersExists(statuses)) {
            statuses = new ArrayList<>();
            Collections.addAll(statuses, DEFAULT_STATUS_FILTER_PARAMS);
        }
        if (!parametersExists(dates)) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusIn(owner, priorities, statuses, pageable);
        } else if (dates.contains(TaskDeadlines.THIS_WEEK) && dates.contains(TaskDeadlines.TOMORROW)) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, pageable, DateHelper.firstDayOfCurrentWeek(), DateHelper.tomorrow());
        } else if (dates.contains(TaskDeadlines.THIS_WEEK) && !dates.contains(TaskDeadlines.TOMORROW)) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, pageable, DateHelper.firstDayOfCurrentWeek(), DateHelper.lastDayOfCurrentWeek());
        } else {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities,
                    statuses, TaskDeadlines.getValues(dates), pageable);
        }

    }
    @Transactional(readOnly = true)
    public Task findTaskById(Long id) {
        return taskRepository.findTasksById(id);
    }

    @Transactional(readOnly = true)
    public long countTasksByFilterParams(Account owner, List<TaskPriority> priorities,
                                         List<TaskStatus> statuses, List<TaskDeadlines> dates) {
        if (!parametersExists(priorities)) {
            priorities = new ArrayList<>();
            Collections.addAll(priorities, DEFAULT_PRIORITY_FILTER_PARAMS);
        }
        if (!parametersExists(statuses)) {
            statuses = new ArrayList<>();
            Collections.addAll(statuses, DEFAULT_STATUS_FILTER_PARAMS);

        }
        if (!parametersExists(dates)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusIn(owner, priorities, statuses);
        } else if (dates.contains(TaskDeadlines.THIS_WEEK) && dates.contains(TaskDeadlines.TOMORROW)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, DateHelper.firstDayOfCurrentWeek(), DateHelper.tomorrow());
        } else if (dates.contains(TaskDeadlines.THIS_WEEK) && !dates.contains(TaskDeadlines.TOMORROW)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, DateHelper.firstDayOfCurrentWeek(), DateHelper.lastDayOfCurrentWeek());
        } else {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities,
                    statuses, TaskDeadlines.getValues(dates));
        }
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
    public void retry(Long taskId, java.util.Date newDeadline) {
        Task task = taskRepository.findTasksById(taskId);
        task.setDeadline(newDeadline);
        task.setStatus(TaskStatus.ACTIVE);
    }

    @Transactional
    public void checkDeadlines(Account account) {
        List<Task> expiredTasks = taskRepository.findAllByOwnerAndDeadlineBefore(account, DateHelper.today());
        for (Task expiredTask : expiredTasks) {
            expiredTask.setStatus(TaskStatus.EXPIRED);
        }
    }

    @Transactional
    public void saveSettings(AccountSettings accountSettings) {
        accountSettingsRepository.save(accountSettings);
    }

    private boolean parametersExists(List filterParams) {
        return filterParams != null && !filterParams.isEmpty();
    }
}
