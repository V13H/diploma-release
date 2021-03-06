package com.hvs.diploma.util;

import org.springframework.validation.Errors;

//Checks whether field empty or not
public class ValidatorHelper {
    private ValidatorHelper() {
    }

    public static boolean isRequiredFieldEmpty(String field, String fieldName, Errors errors) {
        if (field.isEmpty()) {
            errors.rejectValue(fieldName, "value.required");
            return true;
        } else {
            return false;
        }
    }


}
