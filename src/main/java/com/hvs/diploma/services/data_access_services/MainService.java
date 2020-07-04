package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.components.CurrentUser;
import com.hvs.diploma.components.SortAndFilterParams;
import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Achievement;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.services.notification_services.InfoMessagesService;
import com.hvs.diploma.services.notification_services.TurboSmsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class MainService {
    private final TaskService taskService;
    private final AccountService accountService;
    private final TurboSmsService turboSmsService;
    private final InfoMessagesService infoMessagesService;
    private final AchievementService achievementService;


    @Autowired
    public MainService(TaskService taskService, AccountService accountService,
                       TurboSmsService turboSmsService, InfoMessagesService infoMessagesService, AchievementService achievementService) {
        this.taskService = taskService;
        this.accountService = accountService;
        this.turboSmsService = turboSmsService;
        this.infoMessagesService = infoMessagesService;
        this.achievementService = achievementService;
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
    public void updateAccount(CurrentUser currentUser) {
        saveAccount(currentUser.getAccount());
    }


    public List<Task> getTasks(Account owner, Pageable pageable,
                               SortAndFilterParams params) {
        if (params.isFilterParamsSpecified()) {
            return taskService.getTasksByFilterParameters(owner, pageable,
                    params.getPriorities(), params.getStatuses(), params.getDeadlines());
        } else {
            return taskService.getAllUndoneTasksForAccount(owner, pageable);
        }

    }


    public long countTasks(Account owner, SortAndFilterParams params) {
        if (params.isFilterParamsSpecified()) {
            return taskService.countTasksByFilterParams(owner, params.getPriorities(),
                    params.getStatuses(), params.getDeadlines());
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

    public TaskStatistic getTaskStatistic(CurrentUser currentUser) {
        TaskStatistic taskStat = taskService.getTaskStat(currentUser.getAccount());
        long notificationsCount = turboSmsService.countSmsByPhone(currentUser.getPhoneNumber());
        int achievementsUnlocked = currentUser.getAccount().getAchievements().size();
        taskStat.setAchievementsUnlocked(achievementsUnlocked);
        taskStat.setNotificationsCount(notificationsCount);
        return taskStat;
    }

    public List<Achievement> getAllAchievements() {
        return achievementService.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Achievement getAchievementByTitle(String title) {
        return achievementService.findByTitle(title);
    }

    public Set<Achievement> getAchievementsByTitleIsNot(String titleToIgnore) {
        return achievementService.findAllByTitleIsNot(titleToIgnore);
    }

    public void saveAchievement(Achievement achievement) {
        achievementService.save(achievement);
    }

    public double getSmsCreditsBalance() {
        return turboSmsService.getBalance();
    }

    public long countSmsByPhone(String phoneNumber) {
        return turboSmsService.countSmsByPhone(phoneNumber);
    }

    public Task findTaskById(Long taskId) {
        return taskService.findTaskById(taskId);
    }

    public void markTaskAsDoneById(Long id) {
        taskService.markTaskAsDoneById(id);
    }

    public long countTasksByDeadlineAndStatus(Account account, Timestamp date, TaskStatus... statuses) {
        return taskService.countTasksByOwnerAndDeadlineAndStatusIn(account, date, statuses);
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
