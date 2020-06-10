package com.hvs.diploma.components.for_tests;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.AccountSettings;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.enums.UserRole;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.util.DateTimeHelper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
@SuppressWarnings(value = "deprecated")
public class EntityInitializer {
    private final MainService mainService;

    public EntityInitializer(MainService mainService) {
        this.mainService = mainService;
    }

    private void setRandomPriority(int i, Task task) {
        if (i % 3 == 0) {
            task.setPriority(TaskPriority.HIGH);
            task.setPriorityValue(3);
        } else if (i % 2 == 0) {
            task.setPriority(TaskPriority.LOW);
            task.setPriorityValue(1);
        } else {
            task.setPriority(TaskPriority.MEDIUM);
            task.setPriorityValue(2);
        }
    }

    private Account initUser(String email, String phone, UserRole role) {
        AccountSettings settings = new AccountSettings("priorityValue", "ascending");
        Account account = new Account();
        settings.setAccount(account);
        account.setEmail(email);
        account.setPassword("$2y$12$EVY0bHxVz2Q9NyVlnij9/.B0gdsmb0AR0GF29vhOsiTRYtn0exlr6");
        account.setPictureUrl("/img/anonymous-user-svg.svg");
        account.setUserName(email);
        account.setPhoneNumber(phone);
        account.setRole(role);
        account.setHasWatchedGreetingsMessage(true);
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        mainService.saveAccount(account);
        return account;
    }

    private void initTasks(Account account) {
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setOwner(account);
            setRandomPriority(i, task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task #" + i);
            task.setCreationDate(Date.valueOf(LocalDate.now()));
            task.setDeadline(DateTimeHelper.today());
            mainService.saveTask(task);
        }
        for (int i = 6; i <= 10; i++) {
            Task task = new Task();
            task.setOwner(account);
            setRandomPriority(i, task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task #" + i);
            task.setCreationDate(Date.valueOf(LocalDate.now()));
            task.setDeadline(DateTimeHelper.tomorrow());
            mainService.saveTask(task);
        }
        for (int i = 19; i <= 24; i++) {
            Task task = new Task();
            task.setOwner(account);
            setRandomPriority(i, task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task #" + i + "Some kind of description");
            task.setCreationDate(Date.valueOf(LocalDate.now()));
            task.setDeadline(new GregorianCalendar(2020, Calendar.MAY, DateTimeHelper.tomorrow().getDay() + i).getTime());
            mainService.saveTask(task);
        }
    }

    public void initData() {
        Account user = initUser("junior@gmail.com", "+380961212049", UserRole.ROLE_COMMON_USER);
        Account admin = initUser("admin@gmail.com", null, UserRole.ROLE_ADMIN);
        initTasks(user);
        for (int i = 1; i < 11; i++) {
            String email = "user" + i + "@gmail.com";
            initUser(email, null, UserRole.ROLE_COMMON_USER);
        }
    }
}
