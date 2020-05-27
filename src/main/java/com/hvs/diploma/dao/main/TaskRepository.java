package com.hvs.diploma.dao.main;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByOwner(Account owner, Pageable pageable);

    List<Task> findAllByOwnerAndDeadlineBefore(Account account, Timestamp date);

    List<Task> findAllByOwnerAndStatusIsNot(Account owner, TaskStatus status, Pageable pageable);

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

    long countTasksByOwnerAndPriorityInAndStatusIn(Account owner, List<TaskPriority> priorities, List<TaskStatus> statuses);

    long countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(Account owner, List<TaskPriority> priorities,
                                                                List<TaskStatus> statuses, List<Timestamp> deadlines);

    long countTasksByOwner(Account account);

    long countTasksByOwnerAndStatusIsNot(Account account, TaskStatus status);

    void deleteTaskById(Long id);

    Task findTasksById(Long id);

}
