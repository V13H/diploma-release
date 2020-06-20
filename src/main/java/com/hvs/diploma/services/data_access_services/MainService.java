package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.SortAndFilterParams;
import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MainService {
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


    public List<Task> getTasks(Account owner, Pageable pageable,
                               SortAndFilterParams params) {
        if (params.isFilterParamsSpecified()) {
            return taskService.getTasksByFilterParameters(owner, pageable, params);
        } else {
            return taskService.getAllUndoneTasksForAccount(owner, pageable);
        }

    }

    public Task findTaskById(Long id) {
        return taskService.findTaskById(id);
    }


    public long countTasks(Account owner, SortAndFilterParams params) {
        if (params.isFilterParamsSpecified()) {
            return taskService.countTasksByFilterParams(owner, params);
        } else {
            return taskService.countTasksByStatusIsNot(owner, TaskStatus.DONE);
        }
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


    public List<Account> findUserAccounts(PageRequest pageRequest) {
        return accountService.findUserAccounts(pageRequest);
    }

    public Account findAccountById(Long id) {
        return accountService.findAccountById(id);
    }

    public long countTasksByOwnerAndStatus(Account owner, TaskStatus status) {
        return taskService.countTasksByStatus(owner, status);
    }

    public long getUsersCount() {
        return accountService.getUsersCount();
    }

    public long getTasksCount() {
        return taskService.countAllTasks();
    }

    public TaskStatistic getTaskStatistic(CurrentAccount currentAccount) {
        if (taskService.countTasksByOwner(currentAccount.getAccountEntity()) > 0) {
            return taskService.getTaskStat(currentAccount.getAccountEntity());
        } else {
            return null;
        }

    }
}
