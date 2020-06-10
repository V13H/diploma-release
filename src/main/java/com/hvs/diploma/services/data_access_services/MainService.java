package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.controllers.TaskAppearanceController;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MainService {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TaskAppearanceController.class);
    private final TaskService taskService;
    private final AccountService accountService;


    @Autowired
    public MainService(TaskService taskService, AccountService accountService) {
        this.taskService = taskService;
        this.accountService = accountService;
    }


    public Account findAccountByEmail(String email) {
        return accountService.findAccountByEmail(email);
    }


    public void saveAccount(Account account) {
        accountService.saveAccount(account);
    }


    public Account findAccountBySocialId(String socialId) {
        return accountService.findAccountBySocialId(socialId);
    }


    public List<Account> findAllAccounts() {
        return accountService.findAllAccounts();
    }

    @Transactional
    public void updateAccount(CurrentAccount currentAccount) {
        saveAccount(currentAccount.getAccountEntity());
    }

    public List<Task> getAllUndoneTasksForAccount(Account account, Pageable pageable) {
        return taskService.getAllUndoneTasksForAccount(account, pageable);
    }


    public long countTasksByStatusIsNot(Account account, TaskStatus status) {
        return taskService.countTasksByStatusIsNot(account, status);
    }


    public List<Task> getTasksByFilterParameters(Account owner, Pageable pageable,
                                                 List<TaskPriority> priorities,
                                                 List<TaskStatus> statuses,
                                                 List<TaskDeadlines> dates) {

        return taskService.getTasksByFilterParameters(owner, pageable, priorities, statuses, dates);
    }

    public Task findTaskById(Long id) {
        return taskService.findTaskById(id);
    }


    public long countTasksByFilterParams(Account owner, List<TaskPriority> priorities,
                                         List<TaskStatus> statuses, List<TaskDeadlines> dates) {
        return taskService.countTasksByFilterParams(owner, priorities, statuses, dates);
    }


    public void saveTask(Task task) {
        taskService.saveTask(task);
    }


    public long countTasksByOwner(Account owner) {
        return taskService.countTasksByOwner(owner);
    }


    public void deleteTask(Long id) {
        taskService.deleteTaskById(id);
    }


    public void markTaskAsDone(Long id) {
        taskService.markTaskAsDoneById(id);
    }


    public void retry(Long taskId, java.util.Date newDeadline) {
        taskService.retry(taskId, newDeadline);
    }


    public void checkDeadlines(Account account) {
        taskService.checkDeadlines(account);
    }


}
