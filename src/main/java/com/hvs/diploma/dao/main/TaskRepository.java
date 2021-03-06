package com.hvs.diploma.dao.main;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOwner(Account owner, Pageable pageable);

    List<Task> findAllByOwnerAndDeadlineBefore(Account account, Timestamp date);

    List<Task> findAllByOwnerAndStatusIsNotIn(Account owner, Pageable pageable, TaskStatus... statuses);

    List<Task> findAllByOwnerAndPriorityInAndStatusInAndDeadlineIn(Account owner,
                                                                   List<TaskPriority> priorities,
                                                                   List<TaskStatus> statuses,
                                                                   List<Timestamp> deadlines,
                                                                   Pageable pageable);

    List<Task> findAllByOwnerAndPriorityInAndStatusIn(Account owner, List<TaskPriority> priorities,
                                                      List<TaskStatus> statuses, Pageable pageable);

    List<Task> findAllByOwnerAndPriorityInAndStatusInAndDeadlineBetween(Account owner, List<TaskPriority> priorities,
                                                                        List<TaskStatus> statuses,
                                                                        Pageable pageable, Timestamp from, Timestamp to);

    long countTasksByOwnerAndPriorityInAndStatusInAndDeadlineBetween(Account owner, List<TaskPriority> priorities,
                                                                     List<TaskStatus> statuses,
                                                                     Timestamp from, Timestamp to);

    long countTasksByOwnerAndPriorityInAndStatusIn(Account owner, List<TaskPriority> priorities,
                                                   List<TaskStatus> statuses);

    long countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(Account owner, List<TaskPriority> priorities,
                                                                List<TaskStatus> statuses, List<Timestamp> deadlines);

    long countTasksByOwner(Account account);

    long countTasksByOwnerAndStatusIsNot(Account account, TaskStatus status);

    void deleteTaskById(Long id);

    Task findTasksById(Long id);

    long countTasksByOwnerAndStatus(Account account, TaskStatus status);

    long countTasksByOwnerAndDeadlineLessThanAndStatus(Account owner, Date deadline, TaskStatus status);

    long countTaskByOwnerAndStatus(Account account, TaskStatus status);

    long countTasksByOwnerAndStatusAndDeadlineBetween(Account owner, TaskStatus status, Date deadline, Date deadline2);

    long countTasksByOwnerAndStatusIn(Account account, TaskStatus... taskStatuses);

    long countTasksByOwnerAndPriorityIn(Account account, TaskPriority... taskPriorities);

    long countTasksByOwnerAndDeadlineAndStatusIn(Account owner, Date deadline, TaskStatus... statuses);
}
