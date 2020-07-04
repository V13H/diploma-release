package com.hvs.diploma.util;

import com.hvs.diploma.components.CurrentUser;
import org.springframework.ui.Model;

public class ModelAttrsHelper {
    private ModelAttrsHelper() {
    }

    private static void addHasPhoneAttr(Model model, CurrentUser currentUser) {
        model.addAttribute("hasPhone", currentUser.hasPhoneNumber());
    }

    private static void addHasPasswordAttr(Model model, CurrentUser currentUser) {
        model.addAttribute("hasPassword", currentUser.hasPassword());
    }

    private static void addAccountAttr(Model model, CurrentUser currentUser) {
        model.addAttribute("account", currentUser);
    }

    private static void addStatAttr(Model model, CurrentUser currentUser) {
        model.addAttribute("stat", currentUser.getTaskStatistic());
    }

    private static void addIsAdminAttr(Model model, CurrentUser currentUser) {
        model.addAttribute("isAdmin", currentUser.isAdmin());
    }

    public static void addHasPasswAndPhoneAttrs(Model model, CurrentUser currentUser) {
        addHasPasswordAttr(model, currentUser);
        addHasPhoneAttr(model, currentUser);
    }

    public static void addNewAchievementsAttr(Model model, CurrentUser currentUser) {
        model.addAttribute("newAchievements", currentUser.getNewAchievements());
    }

    public static void addBasicDataToModel(Model model, CurrentUser currentUser) {
        addAccountAttr(model, currentUser);
        addStatAttr(model, currentUser);
        addIsAdminAttr(model, currentUser);
        addNewAchievementsAttr(model, currentUser);
    }


}
