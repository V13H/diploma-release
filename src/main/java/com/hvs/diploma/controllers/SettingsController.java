package com.hvs.diploma.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String getSettingsPage(Model model) {

        return "/settings";
    }
    //TODO /change-password and enable-notifications functionality
}
