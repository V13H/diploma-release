package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentUser;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.enums.UserRole;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.form_validators.LoginFormValidator;
import com.hvs.diploma.util.DateTimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

@Controller
public class AuthenticationController {
    private final MainService mainService;
    private final LoginFormValidator loginFormValidator;
    private final CurrentUser currentUser;
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    public AuthenticationController(MainService mainService, LoginFormValidator loginFormValidator, CurrentUser currentUser) {
        this.mainService = mainService;
        this.loginFormValidator = loginFormValidator;
        this.currentUser = currentUser;
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
            currentUser.setAccount(accountByEmail);
            logger.warn(currentUser.toString());
            if (accountByEmail.getRole().equals(UserRole.ROLE_ADMIN)) {
                return "redirect:/admin/";
            } else {
                return "redirect:/";
            }

        }
    }

    @PostConstruct
    private void initData() {
        Account admin = initAccount(UserRole.ROLE_ADMIN, "admin@gmail.com", "Admin");
        Account userAcc = initAccount(UserRole.ROLE_COMMON_USER, "junior@gmail.com", "Junior");

        mainService.saveAccount(admin);
        mainService.saveAccount(userAcc);
        for (int i = 1; i < 10; i++) {
            Task task = new Task();
            task.setDeadline(DateTimeHelper.today());
            task.setPriority(TaskPriority.HIGH);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task №" + i);
            task.setOwner(userAcc);
            mainService.saveTask(task);
        }
        Task task2 = new Task();
        Calendar instance = Calendar.getInstance();
        instance.set(2020, 7, 1);
        task2.setDeadline(instance.getTime());
        task2.setPriority(TaskPriority.HIGH);
        task2.setStatus(TaskStatus.EXPIRED);
        task2.setTaskDescription("Task №" + 11);
        task2.setOwner(userAcc);
        mainService.saveTask(task2);


    }

    private static Account initAccount(UserRole role, String email, String userName) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword("$2y$12$EVY0bHxVz2Q9NyVlnij9/.B0gdsmb0AR0GF29vhOsiTRYtn0exlr6");
        account.setPictureUrl("/img/anonymous-user-svg.svg");
        account.setUserName(userName);
        account.setRole(role);
        account.setHasWatchedGreetingsMessage(true);
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));

        return account;
    }
}
