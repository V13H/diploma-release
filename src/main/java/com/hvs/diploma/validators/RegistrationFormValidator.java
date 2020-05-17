package com.hvs.diploma.validators;

import com.hvs.diploma.dto.AccountDTO;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class RegistrationFormValidator implements Validator {
    org.slf4j.Logger logger = LoggerFactory.getLogger(RegistrationFormValidator.class);

    @Override
    public boolean supports(Class<?> aClass) {
        return AccountDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String confirmPassword = accountDTO.getConfirmPassword();
        String password = accountDTO.getPassword();
        String email = accountDTO.getEmail();
        String phoneNumber = accountDTO.getPhoneNumber();

        validateEmail(errors, email);
        validatePasswords(errors, password, confirmPassword);
        validatePhoneNumber(errors, phoneNumber);


    }

    private void validatePasswords(Errors errors, String password, String confirmPassword) {
        if (!fieldIsEmpty(errors, password, "password") && !fieldIsEmpty(errors, confirmPassword, "confirmPassword")) {
            if (!isPasswordValid(password)) {
                errors.rejectValue("password", "value.length");
            } else if (!password.equals(confirmPassword)) {
                errors.rejectValue("confirmPassword", "passwords.match.error");
                errors.rejectValue("password", "passwords.match.error");
            }

        }
    }

    private void validatePhoneNumber(Errors errors, String phoneNumber) {

        if (!phoneNumber.isEmpty()) {
            boolean isValid = false;
            if (phoneNumber.matches("^[0-9]{10}$") || phoneNumber.matches("^\\++[0-9]{12}$")) {
                isValid = true;
            }
            if (!isValid) {
                errors.rejectValue("phoneNumber", "phone.pattern");
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private boolean isAccordingToValidPattern(String email) {
        String emailPattern = "^[a-z0-9_-]+@[a-z]+\\.(com|ua|ru)$";
        return email.matches(emailPattern);
    }

    private void validateEmail(Errors errors, String email) {
        if (!fieldIsEmpty(errors, email, "email")) {
            if (!isAccordingToValidPattern(email)) {
                errors.rejectValue("email", "email.pattern");
            }
        }
    }

    private boolean fieldIsEmpty(Errors errors, String field, String attributeName) {
        if (field.isEmpty()) {
            errors.rejectValue(attributeName, "value.required");
            return true;
        } else {
            return false;
        }
    }
}
