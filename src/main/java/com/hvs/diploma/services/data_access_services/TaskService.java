package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.components.SortAndFilterParams;
import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.dao.main.TaskRepository;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.util.DateTimeHelper;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    org.slf4j.Logger logger = LoggerFactory.getLogger(TaskService.class);
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Transactional(readOnly = true)
    public List<Task> getAllTasksForAccount(Account account, Pageable pageable) {
        return taskRepository.findAllByOwner(account, pageable);
    }
    @Transactional(readOnly = true)
    public List<Task> getTasksByFilterParameters(Account owner, Pageable pageable,
                                                 SortAndFilterParams params) {
        List<TaskPriority> priorities = params.getNotNullPriorities();
        List<Timestamp> deadlines = params.getNotNullDeadlines();
        List<TaskStatus> statuses = params.getNotNullStatuses();

        if (params.hasNoDeadlineParams()) {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusIn(owner, priorities, statuses, pageable);
        } else if (params.getDeadlines().contains(TaskDeadlines.THIS_WEEK)) {
            Timestamp from = deadlines.get(0);
            Timestamp to = deadlines.get(1);
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, pageable, from, to);
        } else {
            return taskRepository.findAllByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities,
                    statuses, deadlines, pageable);
        }
    }

    @Transactional(readOnly = true)
    public long countTasksByFilterParams(Account owner, SortAndFilterParams params) {
        List<TaskPriority> priorities = params.getNotNullPriorities();
        List<TaskStatus> statuses = params.getNotNullStatuses();
        List<Timestamp> deadlines = params.getNotNullDeadlines();
        Timestamp from = deadlines.get(0);
        Timestamp to = deadlines.get(1);

        if (params.hasNoDeadlineParams()) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusIn(owner, priorities, statuses);
        } else if (params.getDeadlines().contains(TaskDeadlines.THIS_WEEK)) {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineBetween(owner, priorities,
                    statuses, from, to);
        } else {
            return taskRepository.countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(owner, priorities,
                    statuses, deadlines);
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
        taskRepository.save(tasksById);
    }
    @Transactional
    public void retry(Long taskId, java.util.Date newDeadline) {
        Task task = taskRepository.findTasksById(taskId);
        task.setDeadline(newDeadline);
        task.setStatus(TaskStatus.ACTIVE);
        taskRepository.save(task);
    }
    @Transactional
    public void checkDeadlines(Account account) {
        List<Task> tasks = taskRepository.findAllByOwnerAndDeadlineBefore(account, DateTimeHelper.today());
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                if (task.getStatus() != TaskStatus.DONE) {
                    task.setStatus(TaskStatus.EXPIRED);
                    taskRepository.save(task);
                }
            }
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

    @Transactional(readOnly = true)
    public long countTasksByStatus(Account account, TaskStatus taskStatus) {
        return taskRepository.countTaskByOwnerAndStatus(account, taskStatus);
    }

    @Transactional
    public TaskStatistic getTaskStat(Account account) {
        TaskStatistic statistic = new TaskStatistic();
        long totalTasksCount = taskRepository.countTasksByOwner(account);
        long activeTasksCount = taskRepository.countTasksByOwnerAndStatus(account, TaskStatus.ACTIVE);
        long doneTasksCount = taskRepository.countTasksByOwnerAndStatus(account, TaskStatus.DONE);
        long expiredTasksCount = taskRepository.countTasksByOwnerAndStatus(account, TaskStatus.EXPIRED);
        statistic.setActiveTasksCount(activeTasksCount);
        statistic.setDoneTasksCount(doneTasksCount);
        statistic.setExpiredTasksCount(expiredTasksCount);
        statistic.setTotalTasksCount(totalTasksCount);
        return statistic;
    }

}
