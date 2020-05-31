package com.hvs.diploma.controllers;

import com.hvs.diploma.pojo.CurrentSessionAccount;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {
    private final CurrentSessionAccount currentSessionAccount;
    org.slf4j.Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    public SettingsController(CurrentSessionAccount currentSessionAccount) {
        this.currentSessionAccount = currentSessionAccount;
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model) {
        logger.warn("Settings controller: " + currentSessionAccount.toString());
        model.addAttribute("hasPassword", currentSessionAccount.hasPassword());
        return "settings";
    }
    //TODO /change-password and enable-notifications functionality
}
