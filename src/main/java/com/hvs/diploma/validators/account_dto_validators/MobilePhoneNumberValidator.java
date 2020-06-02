package com.hvs.diploma.validators.account_dto_validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.enums.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class MobilePhoneNumberValidator extends AccountDtoValidator {

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String phoneNumber = accountDTO.getPhoneNumber();
        if (!phoneNumber.isEmpty()) {
            boolean isValid = false;
            if (phoneNumber.matches("^[0-9]{10}$") || phoneNumber.matches("^\\++[0-9]{12}$")) {
                isValid = true;
            }
            if (!isValid) {
                errors.rejectValue("phoneNumber", ErrorCode.PHONE_PATTERN.getValue());
            }
        }
    }
}
