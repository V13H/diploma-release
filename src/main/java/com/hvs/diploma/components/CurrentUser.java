package com.hvs.diploma.components;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

//Account wrapper which is created for each session and loads account data from the database
// each time the user signs in
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class CurrentUser {
    private Account account;
    private boolean deadlinesChecked;
    private TaskStatistic taskStatistic;
    private short newAchievements;

    public boolean hasPassword() {
        return account.getPassword() != null;
    }

    public String getEmail() {
        return account.getEmail();
    }

    public String getPassword() {
        return account.getPassword();
    }

    public String getPhoneNumber() {
        return account.getPhoneNumber();
    }

    public String getSocialId() {
        return account.getSocialId();
    }

    public String getPictureUrl() {
        return account.getPictureUrl();
    }

    public boolean hasWatchedGreetingsMessage() {
        return account.hasWatchedGreetingsMessage();
    }

    public boolean hasPhoneNumber() {
        return account.getPhoneNumber() != null;
    }


    public void setPhoneNumber(String phoneNumber) {
        account.setPhoneNumber(phoneNumber);
    }

    public void setPassword(String encodedPassword) {
        account.setPassword(encodedPassword);
    }

    @Override
    public String toString() {
        return "CurrentAccount{" +
                "\nUsername: " + account.getUserName() + "\nRole: " + account.getRole() + "\nSocialId: " +
                account.getSocialId() + "\nPhone: " + account.getPhoneNumber() + "\n}";
    }

    public boolean isAdmin() {
        return account.getRole() == UserRole.ROLE_ADMIN;
    }

    public String getUserName() {
        return account.getUserName();
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        account.setLastSeen(Timestamp.valueOf(lastSeen));
    }

    public void incrementNewAchievementsCount() {
        newAchievements++;
    }

    public void resetNewAchievementsCount() {
        newAchievements = 0;
    }
}
