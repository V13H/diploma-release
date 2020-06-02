package com.hvs.diploma.validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.validators.account_dto_validators.AccountDtoValidator;
import com.hvs.diploma.validators.account_dto_validators.ConfirmPasswordValidator;
import com.hvs.diploma.validators.account_dto_validators.EmailValidator;
import com.hvs.diploma.validators.account_dto_validators.MobilePhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class RegistrationFormValidator extends AccountDtoValidator {
    private final MainService mainService;
    private final MobilePhoneNumberValidator phoneNumberValidator;
    private final EmailValidator emailValidator;
    private final ConfirmPasswordValidator confirmPasswordValidator;

    @Autowired
    public RegistrationFormValidator(MobilePhoneNumberValidator phoneNumberValidator, EmailValidator emailValidator, ConfirmPasswordValidator confirmPasswordValidator, MainService mainService) {
        this.phoneNumberValidator = phoneNumberValidator;
        this.emailValidator = emailValidator;
        this.confirmPasswordValidator = confirmPasswordValidator;
        this.mainService = mainService;
    }


    @Override
    public void validate(Object o, Errors errors) {
        phoneNumberValidator.validate(o, errors);
        emailValidator.validate(o, errors);
        confirmPasswordValidator.validate(o, errors);
        AccountDTO accountDTO = (AccountDTO) o;
        String email = accountDTO.getEmail();
        if (userWithSpecifiedEmailAlreadyExists(email)) {
            errors.rejectValue("email", "email.already-exists");
        }
    }

    private boolean userWithSpecifiedEmailAlreadyExists(String email) {
        return mainService.findAccountByEmail(email) != null;
    }

}
