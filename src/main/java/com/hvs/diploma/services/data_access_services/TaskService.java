package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.dao.main.TaskRepository;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.util.DateTimeHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TaskService {
    private static final TaskStatus[] DEFAULT_STATUS_FILTER_PARAMS = new TaskStatus[]{TaskStatus.ACTIVE, TaskStatus.EXPIRED};
    private static final TaskPriority[] DEFAULT_PRIORITY_FILTER_PARAMS = new TaskPriority[]
            {TaskPriority.HIGH, TaskPriority.MEDIUM, TaskPriority.LOW};

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Transactional(readOnly = true)
    public List<Task> getAllTasksForAccount(Account account, Pageable pageable) {
        return taskRepository.findAllByOwner(account, pageable);
    }
    @Transactional(readOnly = true)
    public List<Task> getTasksByFilterParameters(Account owner, Pageable pageable,
                                                 List<TaskPriority> priorities,
                                                 List<TaskStatus> statuses,
                                                 List<TaskDeadlines> dates) {
        if (parametersNotExists(priorities)) {
            priorities = new ArrayList<>();
            Collections.addAll(priorities, DEFAULT_PRIORITY_FILTER_PARAMS);
        }
        if (parametersNotExists(statuses)) {
            statuses = new ArrayList<>();
            Collections.addAll(statuses, DEFAULT_STATUS_FILTER_PARAMS);
        }
        if (parametersNotExists(dates)) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusIn(owner, priorities, statuses, pageable);
        } else if (dateParamsContains(true, true, dates)) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, pageable, DateTimeHelper.firstDayOfCurrentWeek(), DateTimeHelper.tomorrow());
        } else if (dateParamsContains(true, false, dates)) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, pageable, DateTimeHelper.firstDayOfCurrentWeek(), DateTimeHelper.lastDayOfCurrentWeek());
        } else {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities,
                    statuses, TaskDeadlines.getValues(dates), pageable);
        }
    }
    @Transactional(readOnly = true)
    public long countTasksByFilterParams(Account owner, List<TaskPriority> priorities,
                                         List<TaskStatus> statuses, List<TaskDeadlines> dates) {
        if (parametersNotExists(priorities)) {
            priorities = new ArrayList<>();
            Collections.addAll(priorities, DEFAULT_PRIORITY_FILTER_PARAMS);
        }
        if (parametersNotExists(statuses)) {
            statuses = new ArrayList<>();
            Collections.addAll(statuses, DEFAULT_STATUS_FILTER_PARAMS);

        }
        if (parametersNotExists(dates)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusIn(owner, priorities, statuses);
        } else if (dateParamsContains(true, true, dates)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, DateTimeHelper.firstDayOfCurrentWeek(), DateTimeHelper.tomorrow());
        } else if (dateParamsContains(true, false, dates)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, DateTimeHelper.firstDayOfCurrentWeek(), DateTimeHelper.lastDayOfCurrentWeek());
        } else {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities,
                    statuses, TaskDeadlines.getValues(dates));
        }
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
        List<Task> expiredTasks = taskRepository.findAllByOwnerAndDeadlineBefore(account, DateTimeHelper.today());
        for (Task expiredTask : expiredTasks) {
            expiredTask.setStatus(TaskStatus.EXPIRED);
        }
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
    public Task findTaskById(Long id) {
        return taskRepository.findTasksById(id);
    }

    @Transactional(readOnly = true)
    public long countAllTasks() {
        return taskRepository.count();
    }

    private boolean dateParamsContains(boolean thisWeek, boolean tomorrow, List<TaskDeadlines> dates) {
        boolean firstCondTrue = dates.contains(TaskDeadlines.THIS_WEEK) == thisWeek;
        boolean secondCondTrue = dates.contains(TaskDeadlines.TOMORROW) == tomorrow;
        return firstCondTrue && secondCondTrue;
    }

    private boolean parametersNotExists(List filterParams) {
        return filterParams == null || filterParams.isEmpty();
    }
}
