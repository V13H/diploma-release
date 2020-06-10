package com.hvs.diploma.services.validation_services.account_dto_validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.enums.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class PasswordValidator extends AccountDtoValidator {
    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String password = accountDTO.getPassword();
        if (!password.isEmpty() && !isPasswordLengthValid(password)) {
            errors.rejectValue("password", ErrorCode.VALUE_LENGTH.getValue());
        }
    }

    private boolean isPasswordLengthValid(String password) {
        return password.length() >= 6;
    }
}
