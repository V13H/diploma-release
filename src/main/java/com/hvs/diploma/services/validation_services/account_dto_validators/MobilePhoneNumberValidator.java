package com.hvs.diploma.services.validation_services.account_dto_validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.enums.ErrorCode;
import com.hvs.diploma.services.data_access_services.AccountService;
import com.hvs.diploma.util.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class MobilePhoneNumberValidator extends AccountDtoValidator {
    private final AccountService accountService;
    private static final String FIELD_NAME = "phoneNumber";
    private static final String UA_SHORT_PHONE_PATTERN = "^[0-9]{10}$";
    private static final String INTERNATIONAL_PHONE_PATTERN = "^\\++[0-9]{12}$";

    @Autowired
    public MobilePhoneNumberValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String phoneNumber = accountDTO.getPhoneNumber();
        boolean isEmpty = ValidatorHelper.isRequiredFieldEmpty(phoneNumber, FIELD_NAME, errors);
        if (!isEmpty) {
            if (!patternIsValid(phoneNumber)) {
                errors.rejectValue(FIELD_NAME, ErrorCode.PHONE_PATTERN.getValue());
            } else if (correspondsToShortPattern(phoneNumber)) {
                accountDTO.setPhoneNumber("+38" + phoneNumber);
            }
            if (numberAlreadyExists(accountDTO.getPhoneNumber())) {
                errors.rejectValue(FIELD_NAME, ErrorCode.PHONE_EXISTS.getValue());
            }
        }
    }

    private boolean patternIsValid(String phoneNumber) {
        return correspondsToShortPattern(phoneNumber) || correspondsToInternationalPattern(phoneNumber);
    }

    private boolean correspondsToShortPattern(String phone) {
        return phone.matches(UA_SHORT_PHONE_PATTERN);
    }

    private boolean correspondsToInternationalPattern(String phone) {
        return phone.matches(INTERNATIONAL_PHONE_PATTERN);
    }

    private boolean numberAlreadyExists(String phoneNumber) {
        return accountService.findAccountByPhone(phoneNumber) != null;
    }
}
