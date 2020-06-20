package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.services.data_access_services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NavigationController {
    private final MainService mainService;
    private final CurrentAccount currentAccount;

    @Autowired
    public NavigationController(CurrentAccount currentAccount, MainService mainService) {
        this.currentAccount = currentAccount;
        this.mainService = mainService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "login";
    }

    @GetMapping("/faq")
    public String getFaqPage(Model model) {
        addBasicDataToModel(model);
        return "faq";
    }

    @GetMapping("/statistic")
    public String getStatisticPage(Model model) {
        addBasicDataToModel(model);
        if (currentAccount.getTaskStatistic() == null) {
            model.addAttribute("message", mainService.getNoStatDataMessage());
        }
        return "statistic";
    }

    @GetMapping("/retry")
    public String showRestartTaskForm(@RequestParam Long taskId, Model model) {
        TaskDTO taskDTO = mainService.findTaskById(taskId).toDTO();
        //setting deadline to null just to not display it in template
        taskDTO.setDeadline(null);
        model.addAttribute("taskDTO", taskDTO);
        model.addAttribute("notificationsEnabled", currentAccount.hasPhoneNumber());
        TaskPriority taskPriority = TaskPriority.valueOf(taskDTO.getPriority());
        //The attributes below are added to model because I want to
        // display priority badge at right-top corner of card.
        //Badge`s color depends on TaskPriority,therefore we need to pass css class to template
        //to set badge`s color and title dynamically
        model.addAttribute("priority", taskPriority.getValueToTemplate());
        model.addAttribute("priorityBadgeColor", taskPriority.getCssClass());
        return "restart-task";

    }

    @GetMapping("/add-task")
    public String getAddNewTaskForm(Model model) {
        model.addAttribute("taskDTO", new TaskDTO());
        model.addAttribute("notificationsEnabled", currentAccount.hasPhoneNumber());

        return "add-task";
    }

    private void addBasicDataToModel(Model model) {
        TaskStatistic taskStatistic = mainService.getTaskStatistic(currentAccount);
        model.addAttribute("account", currentAccount);
        model.addAttribute("stat", taskStatistic);
        model.addAttribute("isAdmin", currentAccount.isAdmin());
    }
}
