package com.hvs.diploma.repositories;

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

    List<Task> findAllByOwnerAndStatusIsNot(Account owner, TaskStatus status, Pageable pageable);

    List<Task> findTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(Account owner,
                                                                     TaskPriority[] priorities,
                                                                     TaskStatus[] statuses,
                                                                     Timestamp[] deadlines,
                                                                     Pageable pageable);

    List<Task> findTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(Account owner,
                                                                     TaskPriority[] priorities,
                                                                     TaskStatus[] statuses,
                                                                     Timestamp[] deadlines);

    long countTasksByOwnerAndPriorityInAndStatusInAndDeadlineIn(Account owner, TaskPriority[] priorities,
                                                                TaskStatus[] statuses, Timestamp[] deadlines);

    long countTasksByOwner(Account account);

    long countTasksByOwnerAndStatusIsNot(Account account, TaskStatus status);

    void deleteTaskById(Long id);

    Task findTasksById(Long id);

}
