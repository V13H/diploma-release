package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentUser;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.AccountAchievement;
import com.hvs.diploma.entities.Achievement;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.util.ModelAttrsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
@Controller
public class NavigationController {
    private final MainService mainService;
    private final CurrentUser currentUser;

    @Autowired
    public NavigationController(CurrentUser currentUser, MainService mainService) {
        this.currentUser = currentUser;
        this.mainService = mainService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "login";
    }

    @GetMapping("/faq")
    public String getFaqPage(Model model) {
        ModelAttrsHelper.addBasicDataToModel(model, currentUser);
        model.addAttribute("faqSelected", true);
        return "faq";
    }

    @GetMapping("/statistic")
    public String getStatisticPage(Model model) {
        ModelAttrsHelper.addBasicDataToModel(model, currentUser);
        model.addAttribute("statSelected", true);
        return "statistic";
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model, @RequestParam(required = false, defaultValue = "0") Integer resultCode,
                                  @RequestParam(required = false, name = "attr") String updatedAttribute) {
        //adds to model information whether account has password and phone number
        ModelAttrsHelper.addBasicDataToModel(model, currentUser);
        ModelAttrsHelper.addHasPasswAndPhoneAttrs(model, currentUser);

        if (resultCode == 1 && updatedAttribute != null) {
            model.addAttribute(updatedAttribute, "Updated successfully!");
        }
        model.addAttribute("settingsSelected", true);
        model.addAttribute("accountDTO", currentUser.getAccount().toDTO());
        return "settings";
    }

    @GetMapping("/achievements")
    public String getAchievementsPage(Model model) {
        Set<AccountAchievement> userAchievements = currentUser.getAccount().getAccountAchievements();
        currentUser.resetNewAchievementsCount();
        List<Achievement> allAchievements = mainService.getAllAchievements();
        allAchievements.removeAll(currentUser.getAccount().getAchievements());
        model.addAttribute("accAch", userAchievements);
        model.addAttribute("lockedAch", allAchievements);
        model.addAttribute("achievementsSelected", true);
        ModelAttrsHelper.addBasicDataToModel(model, currentUser);
        return "achievements";
    }
    @GetMapping("/retry")
    public String showRestartTaskForm(@RequestParam Long taskId, Model model) {
        TaskDTO taskDTO = mainService.findTaskById(taskId).toDTO();
        //setting deadline to null just to not display it in template
        taskDTO.setDeadline(null);
        model.addAttribute("taskDTO", taskDTO);
        model.addAttribute("notificationsEnabled", currentUser.hasPhoneNumber());
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
        model.addAttribute("notificationsEnabled", currentUser.hasPhoneNumber());
        return "add-task";
    }

}
