package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.SortAndFilterParams;
import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.services.notification_services.InfoMessagesService;
import com.hvs.diploma.services.notification_services.TurboSmsService;
import lombok.SneakyThrows;
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
    private final TurboSmsService turboSmsService;
    private final InfoMessagesService infoMessagesService;


    @Autowired
    public MainService(TaskService taskService, AccountService accountService,
                       TurboSmsService turboSmsService, InfoMessagesService infoMessagesService) {
        this.taskService = taskService;
        this.accountService = accountService;
        this.turboSmsService = turboSmsService;
        this.infoMessagesService = infoMessagesService;

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


    @Transactional
    public void updateAccount(CurrentAccount currentAccount) {
        saveAccount(currentAccount.getAccountEntity());
    }


    public List<Task> getTasks(Account owner, Pageable pageable,
                               SortAndFilterParams params) {
        if (params.isFilterParamsSpecified()) {
            return taskService.getTasksByFilterParameters(owner, pageable, params);
        } else {
            return taskService.getAllUndoneTasksForAccount(owner, pageable);
        }

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

    public long getUsersCount() {
        return accountService.getUsersCount();
    }

    public long getTasksCount() {
        return taskService.countAllTasks();
    }

    public TaskStatistic getTaskStatistic(CurrentAccount currentAccount) {
        if (taskService.countTasksByOwner(currentAccount.getAccountEntity()) > 0) {
            TaskStatistic taskStat = taskService.getTaskStat(currentAccount.getAccountEntity());
            long notificationsCount = turboSmsService.countSmsByPhone(currentAccount.getPhoneNumber());
            taskStat.setNotificationsCount(notificationsCount);
            return taskStat;
        } else {
            return null;
        }

    }

    public double getSmsCreditsBalance() {
        return turboSmsService.getBalance();
    }

    public long countSmsByPhone(String phoneNumber) {
        return turboSmsService.countSmsByPhone(phoneNumber);
    }

    public String getNoStatDataMessage() {
        return infoMessagesService.getNoStatDataMessage();
    }

    public Task findTaskById(Long taskId) {
        return taskService.findTaskById(taskId);
    }

    public void markTaskAsDoneById(Long id) {
        taskService.markTaskAsDoneById(id);
    }

    @SneakyThrows
    public void sendSmsNotification(TaskDTO taskDTO) {
        turboSmsService.sendSmsNotification(taskDTO);
    }

    public String getEmptyListMessage(Account account) {
        return infoMessagesService.getEmptyListMessage(account);
    }

    public String getGreetingsMessage(Account account) {
        return infoMessagesService.getGreetingsMessage(account);
    }

}
