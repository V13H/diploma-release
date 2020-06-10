package com.hvs.diploma.services.validation_services.account_dto_validators;

import com.hvs.diploma.dto.AccountDTO;
import org.springframework.validation.Validator;

public abstract class AccountDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(AccountDTO.class);
    }
}
