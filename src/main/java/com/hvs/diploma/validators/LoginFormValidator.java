package com.hvs.diploma.validators;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class LoginFormValidator implements Validator {
    private final MainService mainService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public LoginFormValidator(MainService mainService, BCryptPasswordEncoder encoder) {
        this.mainService = mainService;
        this.encoder = encoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(AccountDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        if (accountDTO.getEmail().isEmpty()) {
            errors.rejectValue("email", "email.empty");
        }
        if (accountDTO.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty");
        }
        Account accountByEmail = mainService.findAccountByEmail(accountDTO.getEmail());
        if (accountByEmail == null) {
            errors.rejectValue("email", "login.user-not-found");
        } else {
            if (!encoder.matches(accountDTO.getPassword(), accountByEmail.getPassword())) {
                errors.rejectValue("password", "login.invalid-password");
            }
        }
    }
}
