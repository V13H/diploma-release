package com.hvs.diploma.enums;

import lombok.Getter;

@Getter
//Contains constants which represents ValidationMessages.properties
//and now there is no need to type it each time they needed
public enum ErrorCode {
    REQUIRED("value.required"),
    VALUE_LENGTH("value.length"),
    INVALID_DEADLINE_DATE("value.date.invalid"),
    EMAIL_PATTERN("email.pattern"),
    EMAIL_EXISTS("email.already-exists"),
    PASSWORDS_MATCH_ERROR("passwords.match.error"),
    PHONE_PATTERN("phone.pattern"),
    USER_NOT_FOUND("login.user-not-found"),
    INVALID_PASSWORD("login.invalid-password"),
    INVALID_TIME_FORMAT("time.invalid-format"),
    INVALID_TIME_VALUE("time.invalid-values"),
    INVALID_DEADLINE_FORMAT("format.date.invalid");

    private String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
