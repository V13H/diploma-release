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
public class CurrentSessionAccount {
    private Account account;

    public boolean hasPassword() {
        return account.getPassword() != null;
    }

    @Override
    public String toString() {
        return "CurrentSessionAccount{" +
                "account=" + account +
                '}';
    }
}
