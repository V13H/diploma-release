package com.hvs.diploma.services.validation_services.form_validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.ErrorCode;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.account_dto_validators.EmailValidator;
import com.hvs.diploma.services.validation_services.account_dto_validators.PasswordValidator;
import com.hvs.diploma.util.ValidatorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//DONE
@Service
public class LoginFormValidator implements Validator {
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final MainService mainService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public LoginFormValidator(MainService mainService, BCryptPasswordEncoder encoder, EmailValidator emailValidator, PasswordValidator passwordValidator) {
        this.mainService = mainService;
        this.encoder = encoder;
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(AccountDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        String email = accountDTO.getEmail();
        String password = accountDTO.getPassword();

        boolean emailIsEmpty = ValidatorHelper.isRequiredFieldEmpty(email, "email", errors);
        boolean passwordIsEmpty = ValidatorHelper.isRequiredFieldEmpty(password, "password", errors);

        if (!emailIsEmpty && !passwordIsEmpty) {
            emailValidator.validate(o, errors);
            Account account = loadAccount(accountDTO);
            if (account == null) {
                errors.rejectValue("email", ErrorCode.USER_NOT_FOUND.getValue());
            } else {
                if (!encoder.matches(accountDTO.getPassword(), account.getPassword())) {
                    errors.rejectValue("password", ErrorCode.INVALID_PASSWORD.getValue());
                }
            }
        }
    }

    private Account loadAccount(AccountDTO accountDTO) {
        return mainService.findAccountByEmail(accountDTO.getEmail());
    }
}
