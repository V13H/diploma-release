package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.data_access_services.AccountService;
import com.hvs.diploma.services.data_access_services.TaskService;
import com.hvs.diploma.services.notification_services.TurboSmsService;
import com.hvs.diploma.util.PageableHelper;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    private final CurrentAccount account;
    private final TurboSmsService turboSmsService;
    private final AccountService accountService;
    private final TaskService taskService;
    org.slf4j.Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

    public AdminDashboardController(CurrentAccount account, TurboSmsService turboSmsService, AccountService accountService, TaskService taskService) {
        this.account = account;
        this.turboSmsService = turboSmsService;
        this.accountService = accountService;
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String getAdminDashboardPage(Model model,
                                        @RequestParam(required = false, defaultValue = "0") Integer page) {
        //adding account to the model just to display username
        // and avatar in hamburger menu for small screens
        model.addAttribute("account", account);
        long usersCount = accountService.getUsersCount();
        int size = 10;
        int pagesCount = PageableHelper.getPagesCount(usersCount, size);
        int pageParam = PageableHelper.checkPageParam(page, usersCount, size);
        List<Account> users = accountService.findUserAccounts(PageRequest.of(pageParam, size));
        logger.warn(users.toString());
        logger.warn("page: " + page);
        logger.warn("page: " + pageParam);
        model.addAttribute("balance", turboSmsService.getBalance());
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("messages", turboSmsService.findAll());
        model.addAttribute("users", users);
        model.addAttribute("tasksCount", taskService.countAllTasks());
        model.addAttribute("page", pageParam);
        model.addAttribute("pagesCount", pagesCount);
        return "admin";
    }
}
