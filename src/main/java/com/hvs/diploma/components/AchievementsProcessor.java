package com.hvs.diploma.components;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.AccountAchievement;
import com.hvs.diploma.entities.Achievement;
import com.hvs.diploma.enums.AchievementData;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.util.DateTimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;


@Component
public class AchievementsProcessor {
    @Autowired
    private MainService mainService;
    @Autowired
    private CurrentUser currentUser;
    private Logger logger = LoggerFactory.getLogger(AchievementsProcessor.class);

    @PostConstruct
    void initAchieves() {
        Achievement accuracy = AchievementData.ACCURACY.getAchievement();
        Achievement aimingHigh = AchievementData.AIMING_HIGH.getAchievement();
        Achievement comeback = AchievementData.COMEBACK.getAchievement();
        Achievement disciplineAchievement = AchievementData.DISCIPLINE.getAchievement();
        Achievement firstWinAchievement = AchievementData.FIRST_WIN.getAchievement();
        Achievement marathonAchievement = AchievementData.MARATHON.getAchievement();
        Achievement startAchievement = AchievementData.START.getAchievement();
        Achievement stepByStepAchievement = AchievementData.STEP_BY_STEP.getAchievement();
        Achievement veteranAchievement = AchievementData.VETERAN.getAchievement();
        Achievement spartanAchievement = AchievementData.SPARTAN.getAchievement();
        Achievement guruAchievement = AchievementData.GURU.getAchievement();
        mainService.saveAchievement(accuracy);
        mainService.saveAchievement(aimingHigh);
        mainService.saveAchievement(comeback);
        mainService.saveAchievement(disciplineAchievement);
        mainService.saveAchievement(firstWinAchievement);
        mainService.saveAchievement(marathonAchievement);
        mainService.saveAchievement(startAchievement);
        mainService.saveAchievement(stepByStepAchievement);
        mainService.saveAchievement(veteranAchievement);
        mainService.saveAchievement(spartanAchievement);
        mainService.saveAchievement(guruAchievement);
    }

    public void process() {
        Account account = currentUser.getAccount();
        if (!hasAllAchievements(account, false)) {
            checkComebackReq(account);
            checkFirstWinReq(account);
            checkMarathonReq(account);
            checkAimingHighReq(account);
            checkDiscReq(account);
            checkAccReq(account);
            checkStepByStep(account);
            checkVeteranReq(account);
            checkSpartanReq(account);

            checkGuruReq(account);
            logger.warn(account.getAccountAchievements().toString());
        }
    }

    public void unlockAll() {
//        Account accountEntity = currentUser.getAccountEntity();
//        if(accountEntity.getEmail().equalsIgnoreCase("user@gmail.com")){
//            List<Achievement> all = achievementRepository.findAll();
//            for (Achievement achievement : all) {
//                accountEntity.addAchievement(achievement);
//            }
//            mainService.saveAccount(accountEntity);
//            currentUser.incrementNewAchievementsCount();
//        }
    }


    private void checkAccReq(Account account) {
        Achievement accuracy = mainService.getAchievementByTitle("Accuracy");
        if (hasNotAchievement(account, accuracy)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            long expiredTasksCount = taskStatistic.getExpiredTasksCount();
            long doneTasksCount = taskStatistic.getDoneTasksCount();
            if (expiredTasksCount == 0 && doneTasksCount == 1) {
                account.addAchievement(accuracy);
                mainService.saveAccount(account);
                currentUser.incrementNewAchievementsCount();
            }
        }
    }

    private void checkDiscReq(Account account) {
        Achievement discipline = mainService.getAchievementByTitle("Discipline");
        if (hasNotAchievement(account, discipline)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            double successRate = 75.0;
            long doneTasksCount = taskStatistic.getDoneTasksCount();
            if (doneTasksCount == 2 && successRate > 74.9) {
                account.addAchievement(discipline);
                mainService.saveAccount(account);
                currentUser.incrementNewAchievementsCount();
            }
        }
    }

    private void checkComebackReq(Account account) {
        Achievement comeback = mainService.getAchievementByTitle("Comeback");
        if (hasNotAchievement(account, comeback)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            if (taskStatistic.getRestartedAndDoneTasksCount() > 0) {
                account.addAchievement(comeback);
                mainService.saveAccount(account);
                currentUser.incrementNewAchievementsCount();
            }

        }
    }

    private void checkAimingHighReq(Account account) {
        Achievement aimingHigh = mainService.getAchievementByTitle("Aiming high");
        if (hasNotAchievement(account, aimingHigh)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            if (taskStatistic.getHighPriorityTasksCount() >= 3) {
                account.addAchievement(aimingHigh);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    private void checkStepByStep(Account account) {
        Achievement stepByStep = mainService.getAchievementByTitle("Step by step");
        if (hasNotAchievement(account, stepByStep)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            if (taskStatistic.getDoneTasksCount() >= 4) {
                account.addAchievement(stepByStep);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    private void checkSpartanReq(Account account) {
        Achievement spartan = mainService.getAchievementByTitle("Spartan");
        if (hasNotAchievement(account, spartan)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            long doneTasksCount = taskStatistic.getDoneTasksCount();
            long expiredTasksCount = taskStatistic.getExpiredTasksCount();
            if (doneTasksCount >= 5 && expiredTasksCount == 0) {
                account.addAchievement(spartan);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    private void checkFirstWinReq(Account account) {
        Achievement firstWin = mainService.getAchievementByTitle("First win");
        if (hasNotAchievement(account, firstWin)) {
            long doneTasksCount = currentUser.getTaskStatistic().getDoneTasksCount();
            if (doneTasksCount > 0) {
                account.addAchievement(firstWin);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    public void checkLiftOffReq(Account account) {
        Achievement start = mainService.getAchievementByTitle("3..2..1..lift off!");
        if (hasNotAchievement(account, start)) {
            long activeTasksCount = currentUser.getTaskStatistic().getActiveTasksCount();
            if (activeTasksCount > 0) {
                account.addAchievement(start);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    private void checkGuruReq(Account account) {
        Achievement guru = mainService.getAchievementByTitle("Guru");
        if (hasNotAchievement(account, guru)) {
            TaskStatistic taskStatistic = currentUser.getTaskStatistic();
            long notActiveTasksCount = taskStatistic.getNotActiveTasksCount();
            long activeTasksCount = taskStatistic.getActiveTasksCount();
            long totalTasksCount = activeTasksCount + notActiveTasksCount;
            if (totalTasksCount > 7 && hasAllAchievements(account, true)) {
                account.addAchievement(guru);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    private void checkVeteranReq(Account account) {
        Achievement veteran = mainService.getAchievementByTitle("Veteran");
        if (hasNotAchievement(account, veteran)) {
            long doneTasksCount = currentUser.getTaskStatistic().getDoneTasksCount();
            if (doneTasksCount >= 6) {
                account.addAchievement(veteran);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }

    private void checkMarathonReq(Account account) {
        Achievement marathon = mainService.getAchievementByTitle("Marathon");
        if (hasNotAchievement(account, marathon)) {
            long activeDailyTasks = mainService.countTasksByDeadlineAndStatus(account,
                    DateTimeHelper.today(), TaskStatus.ACTIVE, TaskStatus.RESTARTED_ACTIVE);
            long doneDailyTasks = mainService.countTasksByDeadlineAndStatus(account, DateTimeHelper.today(),
                    TaskStatus.DONE, TaskStatus.RESTARTED_DONE);
            if (doneDailyTasks >= 5 && activeDailyTasks == 0) {
                account.addAchievement(marathon);
                currentUser.incrementNewAchievementsCount();
                mainService.saveAccount(account);
            }
        }
    }


    private boolean hasAllAchievements(Account account, boolean excludeFinalAchievement) {
        Set<Achievement> achievements = mainService.getAchievementsByTitleIsNot("Guru");
        Set<AccountAchievement> usersAchievements = currentUser.getAccount().getAccountAchievements();
        logger.warn("all ach: " + achievements.size() + "  userAch: " + usersAchievements.size());
        if (excludeFinalAchievement) {
            return usersAchievements.size() == (achievements.size() - 1);
        } else {
            return achievements.size() == usersAchievements.size();
        }
    }

    private boolean hasNotAchievement(Account account, Achievement achievement) {
        logger.warn("Has not " + achievement.getTitle() + " : " + !account.getAchievements().contains(achievement));
        return !account.getAchievements().contains(achievement);
    }

}
