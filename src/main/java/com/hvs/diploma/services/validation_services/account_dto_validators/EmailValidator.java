package com.hvs.diploma.services.validation_services.account_dto_validators;

import com.hvs.diploma.dto.AccountDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EmailValidator extends AccountDtoValidator {
    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String email = accountDTO.getEmail();
        if (!email.isEmpty()) {
            if (!isAccordingToValidPattern(email)) {
                errors.rejectValue("email", "email.pattern");
            }
        }
    }

    private boolean isAccordingToValidPattern(String email) {
        String emailPattern = "^[a-z0-9_-]+@[a-z]+\\.(com|ua|ru)$";
        return email.matches(emailPattern);
    }

}
