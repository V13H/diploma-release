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

//Account wrapper which is created for each session and loads account data from database each time the user signs in
//The main purpose of this class  is to reduce number of requests to database
//Account data loads once and stored in this component
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class CurrentAccount {
    private Account accountEntity;
    private boolean isTasksDeadlinesChecked;

    public boolean hasPassword() {
        return accountEntity.getPassword() != null;
    }

    public String getEmail() {
        return accountEntity.getEmail();
    }

    public String getPassword() {
        return accountEntity.getPassword();
    }

    public String getPhoneNumber() {
        return accountEntity.getPhoneNumber();
    }

    public String getSocialId() {
        return accountEntity.getSocialId();
    }

    public String getPictureUrl() {
        return accountEntity.getPictureUrl();
    }

    public boolean hasWatchedGreetingsMessage() {
        return accountEntity.hasWatchedGreetingsMessage();
    }

    public boolean hasPhoneNumber() {
        return accountEntity.getPhoneNumber() != null;
    }


    public void setPhoneNumber(String phoneNumber) {
        accountEntity.setPhoneNumber(phoneNumber);
    }

    public void setPassword(String encodedPassword) {
        accountEntity.setPassword(encodedPassword);
    }

    @Override
    public String toString() {
        return "CurrentAccount{" +
                "\nUsername: " + accountEntity.getUserName() + "\nRole: " + accountEntity.getRole() + "\nSocialId: " +
                accountEntity.getSocialId() + "\nPhone: " + accountEntity.getPhoneNumber() + "\n}";
    }

    public boolean isAdmin() {
        return accountEntity.getRole() == UserRole.ROLE_ADMIN;
    }

    public String getUserName() {
        return accountEntity.getUserName();
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        accountEntity.setLastSeen(Timestamp.valueOf(lastSeen));
    }
}
