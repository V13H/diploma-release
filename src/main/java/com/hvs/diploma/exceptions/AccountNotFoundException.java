package com.hvs.diploma.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AccountNotFoundException extends AuthenticationException {
    protected String viewName;
    private String attributeName;

    public AccountNotFoundException(String message, String viewName, String attributeName) {
        super(message);
        this.attributeName = attributeName;
        this.viewName = viewName;
    }
}
