package com.hvs.diploma.validators.account_dto_validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.util.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class ConfirmPasswordValidator extends AccountDtoValidator {
    private final PasswordValidator passwordValidator;

    @Autowired
    public ConfirmPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String password = accountDTO.getPassword();
        String confirmPassword = accountDTO.getConfirmPassword();
        boolean passwordIsEmpty = ValidatorHelper.isRequiredFieldEmpty(password, "password", errors);
        if (!passwordIsEmpty) {
            passwordValidator.validate(o, errors);
        }
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword", "passwords.match.error");
            errors.rejectValue("password", "passwords.match.error");
        }
    }
}
