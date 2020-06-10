package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.for_tests.EntityInitializer;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.UserRole;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.form_validators.LoginFormValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthenticationController {
    private final MainService mainService;
    private final LoginFormValidator loginFormValidator;
    private final CurrentAccount currentAccount;
    org.slf4j.Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private EntityInitializer initializer;

    @Autowired
    public AuthenticationController(MainService mainService, LoginFormValidator loginFormValidator, CurrentAccount currentAccount) {
        this.mainService = mainService;
        this.loginFormValidator = loginFormValidator;
        this.currentAccount = currentAccount;
    }


    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "login";
    }

    @PostMapping("/signIn")
    public String signIn(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                         BindingResult bindingResult,
                         HttpServletRequest request) throws ServletException {
        loginFormValidator.validate(accountDTO, bindingResult);
        logger.warn("authContr signIn called: ");
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


    @GetMapping("/users")
    public String getUsersDataList(Model model) {
        List<Account> allAccounts = mainService.findAllAccounts();
        model.addAttribute("accounts", allAccounts);
        return "users";
    }



    @PostConstruct
    private void initTestData() {
        initializer.initData();
    }
}
