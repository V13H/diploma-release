package com.hvs.diploma.controllers;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.AccountSettings;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.util.DateHelper;
import com.hvs.diploma.validators.LoginFormValidator;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
public class AuthenticationController {
    private final MainService mainService;
    private final LoginFormValidator loginFormValidator;

    @Autowired
    public AuthenticationController(MainService mainService, LoginFormValidator loginFormValidator) {
        this.mainService = mainService;
        this.loginFormValidator = loginFormValidator;
    }


    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "/login";
    }

    @PostMapping("/signIn")
    public String signIn(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                         BindingResult bindingResult,
                         HttpServletRequest request) throws ServletException {
        loginFormValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/login";
        } else {
            request.login(accountDTO.getEmail(), accountDTO.getPassword());
            return "redirect:/";
        }
    }

    @PostConstruct
    private void initData() {
        AccountSettings settings = new AccountSettings("priorityValue", "ascending");
        Account account = new Account();
        settings.setAccount(account);
        account.setEmail("junior@gmail.com");
        account.setPassword("$2y$12$EVY0bHxVz2Q9NyVlnij9/.B0gdsmb0AR0GF29vhOsiTRYtn0exlr6");
        account.setPictureUrl("/img/anonymous-user-svg.svg");
        account.setUserName("junior@gmail.com");
//        account.setSettings(settings);
        mainService.saveAccount(account);


//        Account user = new Account();
//        user.setEmail("user@gmail.com");
//        user.setPictureUrl("/img/anonymous-user-svg.svg");
//        user.setPassword("$2y$12$EVY0bHxVz2Q9NyVlnij9/.B0gdsmb0AR0GF29vhOsiTRYtn0exlr6");
//        user.setUserName("user@gmail.com");
//        settings.setAccount(user);
//        user.setSettings(settings);
//        mainService.saveAccount(user);
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setOwner(account);
            setRandomPriority(i, task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task #" + i);
            task.setCreationDate(Date.valueOf(LocalDate.now()));
            task.setDeadline(DateHelper.today());
            mainService.saveTask(task);
        }
        for (int i = 6; i <= 10; i++) {
            Task task = new Task();
            task.setOwner(account);
            setRandomPriority(i, task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task #" + i);
            task.setCreationDate(Date.valueOf(LocalDate.now()));
            task.setDeadline(DateHelper.tomorrow());
            mainService.saveTask(task);
        }
        for (int i = 19; i <= 24; i++) {
            Task task = new Task();
            task.setOwner(account);
            setRandomPriority(i, task);
            task.setStatus(TaskStatus.ACTIVE);
            task.setTaskDescription("Task #" + i + "Some kind of description");
            task.setCreationDate(Date.valueOf(LocalDate.now()));
            task.setDeadline(new GregorianCalendar(2020, Calendar.MAY, DateHelper.tomorrow().getDay() + i).getTime());
            mainService.saveTask(task);
        }
//        for (int i = 16; i <=20 ; i++) {
//            Task task = new Task();
//            task.setOwner(account);
//            task.setPriority(TaskPriority.HIGH);
//            task.setStatus(TaskStatus.EXPIRED);
//            task.setPriorityValue(1);
//            task.setTaskDescription("Task #"+i);
//            task.setCreationDate(Date.valueOf(LocalDate.now()));
//            task.setDeadline(new GregorianCalendar(2020, Calendar.MARCH,i).getTime());
//            accountService.saveTask(task);
//        }
    }

    @GetMapping("/users")
    public String getUsersDataList(Model model) {
        List<Account> allAccounts = mainService.findAllAccounts();
        model.addAttribute("accounts", allAccounts);
        return "users";
    }

    private void setRandomPriority(int i, Task task) {
        if (i % 3 == 0) {
            task.setPriority(TaskPriority.HIGH);
            task.setPriorityValue(3);
        } else if (i % 2 == 0) {
            task.setPriority(TaskPriority.LOW);
            task.setPriorityValue(1);
        } else {
            task.setPriority(TaskPriority.MEDIUM);
            task.setPriorityValue(2);
        }
    }
}
