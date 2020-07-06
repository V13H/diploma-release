//package com.hvs.diploma.components;
//
//import com.hvs.diploma.entities.Account;
//import com.hvs.diploma.entities.Achievement;
//import com.hvs.diploma.entities.Task;
//import com.hvs.diploma.enums.AchievementData;
//import com.hvs.diploma.enums.TaskPriority;
//import com.hvs.diploma.enums.TaskStatus;
//import com.hvs.diploma.enums.UserRole;
//import com.hvs.diploma.services.data_access_services.MainService;
//import com.hvs.diploma.util.DateTimeHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.Calendar;
//
//@Component
//public class DataInitializer {
//    private final MainService mainService;
//
//    public DataInitializer(MainService mainService) {
//        this.mainService = mainService;
//    }
//
//    @PostConstruct
//    private void initData() {
//        Account admin = initAccount(UserRole.ROLE_ADMIN, "admin@gmail.com", "Admin");
//        Account userAcc = initAccount(UserRole.ROLE_COMMON_USER, "junior@gmail.com", "Junior");
//
//        mainService.saveAccount(admin);
//        mainService.saveAccount(userAcc);
//        for (int i = 1; i < 10; i++) {
//            Task task = new Task();
//            task.setDeadline(DateTimeHelper.today());
//            task.setPriority(TaskPriority.HIGH);
//            task.setPriorityValue(3);
//            task.setStatus(TaskStatus.ACTIVE);
//            task.setTaskDescription("Task №" + i);
//            task.setOwner(userAcc);
//            mainService.saveTask(task);
//        }
//        Task task2 = new Task();
//        Calendar instance = Calendar.getInstance();
//        instance.set(2020, 7, 1);
//        task2.setDeadline(instance.getTime());
//        task2.setPriority(TaskPriority.HIGH);
//        task2.setPriorityValue(3);
//        task2.setStatus(TaskStatus.EXPIRED);
//        task2.setTaskDescription("Task №" + 11);
//        task2.setOwner(userAcc);
//        mainService.saveTask(task2);
//
//
//    }
//
//    private static Account initAccount(UserRole role, String email, String userName) {
//        Account account = new Account();
//        account.setEmail(email);
//        account.setPassword("$2y$12$EVY0bHxVz2Q9NyVlnij9/.B0gdsmb0AR0GF29vhOsiTRYtn0exlr6");
//        account.setPictureUrl("/img/anonymous-user-svg.svg");
//        account.setUserName(userName);
//        account.setRole(role);
//        account.setHasWatchedGreetingsMessage(true);
//        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
//        return account;
//    }
//    @PostConstruct
//    void initAchieves() {
//        Achievement accuracy = AchievementData.ACCURACY.getAchievement();
//        Achievement aimingHigh = AchievementData.AIMING_HIGH.getAchievement();
//        Achievement comeback = AchievementData.COMEBACK.getAchievement();
//        Achievement disciplineAchievement = AchievementData.DISCIPLINE.getAchievement();
//        Achievement firstWinAchievement = AchievementData.FIRST_WIN.getAchievement();
//        Achievement marathonAchievement = AchievementData.MARATHON.getAchievement();
//        Achievement startAchievement = AchievementData.START.getAchievement();
//        Achievement stepByStepAchievement = AchievementData.STEP_BY_STEP.getAchievement();
//        Achievement veteranAchievement = AchievementData.VETERAN.getAchievement();
//        Achievement spartanAchievement = AchievementData.SPARTAN.getAchievement();
//        Achievement guruAchievement = AchievementData.GURU.getAchievement();
//        mainService.saveAchievement(startAchievement);
//        mainService.saveAchievement(firstWinAchievement);
//        mainService.saveAchievement(accuracy);
//        mainService.saveAchievement(disciplineAchievement);
//        mainService.saveAchievement(aimingHigh);
//        mainService.saveAchievement(marathonAchievement);
//        mainService.saveAchievement(stepByStepAchievement);
//        mainService.saveAchievement(comeback);
//        mainService.saveAchievement(veteranAchievement);
//        mainService.saveAchievement(spartanAchievement);
//        mainService.saveAchievement(guruAchievement);
//    }
//}
