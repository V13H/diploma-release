package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.account_dto_validators.ConfirmPasswordValidator;
import com.hvs.diploma.services.validation_services.account_dto_validators.MobilePhoneNumberValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class SettingsController {
    private final CurrentAccount currentAccount;
    private final MobilePhoneNumberValidator phoneNumberValidator;
    private final ConfirmPasswordValidator passwordValidator;
    private final MainService mainService;
    private final BCryptPasswordEncoder passwordEncoder;
    org.slf4j.Logger logger = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    public SettingsController(CurrentAccount currentAccount, MobilePhoneNumberValidator phoneNumberValidator, ConfirmPasswordValidator passwordValidator, MainService mainService, BCryptPasswordEncoder passwordEncoder) {
        this.currentAccount = currentAccount;
        this.phoneNumberValidator = phoneNumberValidator;
        this.passwordValidator = passwordValidator;
        this.mainService = mainService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model, @RequestParam(required = false, defaultValue = "0") Integer resultCode,
                                  @RequestParam(required = false, name = "attr") String updatedAttribute) {
        logger.warn(currentAccount.toString());
        //adds to model information whether account has password and phone number
        addNotificationsAndPasswordAttributes(model);
        if (resultCode == 1 && updatedAttribute != null) {
            model.addAttribute(updatedAttribute, "Updated successfully!");
        }
        model.addAttribute("accountDTO", currentAccount.getAccountEntity().toDTO());
        model.addAttribute("isAdmin", currentAccount.isAdmin());
        model.addAttribute("stat", currentAccount.getTaskStatistic());
        model.addAttribute("account", currentAccount.getAccountEntity());
        return "settings";
    }

    @PostMapping("/update-password")
    public String updatePassword(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO, Model model,
                                 BindingResult bindingResult) {
        passwordValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            addNotificationsAndPasswordAttributes(model);
            return "settings";
        } else {
            logger.warn(accountDTO.toString());
            String newPassword = accountDTO.getConfirmPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            currentAccount.setPassword(encodedPassword);
            mainService.updateAccount(currentAccount);
            logger.warn("updated: " + currentAccount.toString());
            return "redirect:/settings?resultCode=1&attr=passwordAttr";
        }
    }

    @PostMapping("update-phone")
    public String updatePhone(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO, Model model,
                              BindingResult bindingResult) {
        phoneNumberValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            addNotificationsAndPasswordAttributes(model);
            return "settings";
        } else {
            currentAccount.setPhoneNumber(accountDTO.getPhoneNumber());
            mainService.updateAccount(currentAccount);
            return "redirect:/settings?resultCode=1&attr=phoneAttr";
        }
    }

    private void addNotificationsAndPasswordAttributes(Model model) {
        addHasPhoneAttrToModel(model);
        addHasPasswordAttrToModel(model);
        model.addAttribute("account", currentAccount);
        model.addAttribute("isAdmin", currentAccount.isAdmin());
        model.addAttribute("stat", currentAccount.getTaskStatistic());

    }

    private void addHasPasswordAttrToModel(Model model) {
        model.addAttribute("hasPassword", currentAccount.hasPassword());
    }

    private void addHasPhoneAttrToModel(Model model) {
        model.addAttribute("hasPhone", currentAccount.hasPhoneNumber());
    }

}
