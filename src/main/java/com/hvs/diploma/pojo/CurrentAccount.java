package com.hvs.diploma.pojo;

import com.hvs.diploma.entities.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class CurrentAccount {
    private Account account;
    private boolean isTasksDeadlinesChecked;

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
}
