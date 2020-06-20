package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.for_tests.EntityInitializer;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.UserRole;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.form_validators.LoginFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AuthenticationController {
    private final MainService mainService;
    private final LoginFormValidator loginFormValidator;
    private final CurrentAccount currentAccount;
    @Autowired
    private EntityInitializer initializer;

    @Autowired
    public AuthenticationController(MainService mainService, LoginFormValidator loginFormValidator, CurrentAccount currentAccount) {
        this.mainService = mainService;
        this.loginFormValidator = loginFormValidator;
        this.currentAccount = currentAccount;
    }

    @PostMapping("/signIn")
    public String signIn(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                         BindingResult bindingResult,
                         HttpServletRequest request) throws ServletException {
        loginFormValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        } else {
            request.login(accountDTO.getEmail(), accountDTO.getPassword());
            Account accountByEmail = mainService.findAccountByEmail(accountDTO.getEmail());
            currentAccount.setAccountEntity(accountByEmail);
            if (accountByEmail.getRole().equals(UserRole.ROLE_ADMIN)) {
                return "redirect:/admin/";
            } else {
                return "redirect:/";
            }

        }
    }

    @PostConstruct
    private void initTestData() {
        initializer.initData();
    }
}
