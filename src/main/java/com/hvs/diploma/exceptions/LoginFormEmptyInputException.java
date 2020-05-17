package com.hvs.diploma.exceptions;

import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class LoginFormEmptyInputException extends IOException {
    private Map<String, String> attributesMessagesMap;
    private String viewName = "login";

    public LoginFormEmptyInputException(String viewName, String email, String password) {
        this.viewName = viewName;
        initMessagesMap(email, password);

    }

    public LoginFormEmptyInputException(String email, String password) {
        initMessagesMap(email, password);
    }

    private void initMessagesMap(String email, String password) {
        attributesMessagesMap = new HashMap<>();
        if (email.isEmpty()) {
            attributesMessagesMap.put("emailError", "Enter email");
        }
        if (password.isEmpty()) {
            attributesMessagesMap.put("email", email);
            attributesMessagesMap.put("passwordError", "Enter password");
        }
    }

    public String getMessage(String attributeName) {
        return attributesMessagesMap.get(attributeName);
    }
}
