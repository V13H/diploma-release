package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentUser;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.account_dto_validators.ConfirmPasswordValidator;
import com.hvs.diploma.services.validation_services.account_dto_validators.MobilePhoneNumberValidator;
import com.hvs.diploma.util.ModelAttrsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SettingsController {
    private final CurrentUser currentUser;
    private final MobilePhoneNumberValidator phoneNumberValidator;
    private final ConfirmPasswordValidator passwordValidator;
    private final MainService mainService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SettingsController(CurrentUser currentUser, MobilePhoneNumberValidator phoneNumberValidator, ConfirmPasswordValidator passwordValidator, MainService mainService, BCryptPasswordEncoder passwordEncoder) {
        this.currentUser = currentUser;
        this.phoneNumberValidator = phoneNumberValidator;
        this.passwordValidator = passwordValidator;
        this.mainService = mainService;
        this.passwordEncoder = passwordEncoder;
    }



    @PostMapping("/update-password")
    public String updatePassword(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO, Model model,
                                 BindingResult bindingResult) {
        passwordValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAttrsHelper.addHasPasswAndPhoneAttrs(model, currentUser);
            return "settings";
        } else {
            String newPassword = accountDTO.getConfirmPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            currentUser.setPassword(encodedPassword);
            mainService.updateAccount(currentUser);
            return "redirect:/settings?resultCode=1&attr=passwordAttr";
        }
    }

    @PostMapping("update-phone")
    public String updatePhone(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO, Model model,
                              BindingResult bindingResult) {
        phoneNumberValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            ModelAttrsHelper.addHasPasswAndPhoneAttrs(model, currentUser);
            return "settings";
        } else {
            currentUser.setPhoneNumber(accountDTO.getPhoneNumber());
            mainService.updateAccount(currentUser);
            return "redirect:/settings?resultCode=1&attr=phoneAttr";
        }
    }



}
