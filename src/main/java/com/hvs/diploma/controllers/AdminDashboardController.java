package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.notification_services.TurboSmsService;
import com.hvs.diploma.util.PageableHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MainService mainService;
    org.slf4j.Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

    @Autowired
    public AdminDashboardController(CurrentAccount account, TurboSmsService turboSmsService, MainService mainService) {
        this.account = account;
        this.turboSmsService = turboSmsService;
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String getAdminDashboardPage(Model model,
                                        @RequestParam(required = false, defaultValue = "0") Integer page) {
        //adding account to the model just to display username
        // and avatar in hamburger menu for small screens
        model.addAttribute("account", account);
        long usersCount = mainService.getUsersCount();
        long tasksCount = mainService.getTasksCount();
        int size = 10;
        int pagesCount = PageableHelper.getPagesCount(usersCount, size);
        int pageParam = PageableHelper.checkPageParam(page, usersCount, size);
        List<Account> users = mainService.findUserAccounts(PageRequest.of(pageParam, size));
        model.addAttribute("balance", turboSmsService.getBalance());
        model.addAttribute("usersCount", usersCount);
        model.addAttribute("tasksCount", tasksCount);
        model.addAttribute("messages", turboSmsService.findAll());
        model.addAttribute("users", users);
        model.addAttribute("page", pageParam);
        model.addAttribute("pagesCount", pagesCount);
        return "admin";
    }

    @GetMapping("/details")
    public String getUserDetailsPage(@RequestParam Long id, Model model) {
        Account accountById = mainService.findAccountById(id);
        long tasksCount = mainService.countTasksByOwner(accountById);
        long notificationsCount = 0;
        boolean hasPhone = accountById.getPhoneNumber() != null;
        if (hasPhone) {
            notificationsCount = turboSmsService.countByPhone(accountById.getPhoneNumber());
        }
        model.addAttribute("account", accountById);
        model.addAttribute("hasPhone", hasPhone);
        model.addAttribute("tasksCount", tasksCount);
        model.addAttribute("notificationsCount", notificationsCount);
        return "user-details";
    }
}
