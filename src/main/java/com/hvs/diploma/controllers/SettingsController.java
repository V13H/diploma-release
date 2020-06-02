package com.hvs.diploma.controllers;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.pojo.CurrentAccount;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.validators.account_dto_validators.ConfirmPasswordValidator;
import com.hvs.diploma.validators.account_dto_validators.MobilePhoneNumberValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String getSettingsPage(Model model) {
        model.addAttribute("hasPassword", currentAccount.hasPassword());
        model.addAttribute("accountDTO", currentAccount.getAccount().toDTO());
        return "settings";
    }

    @PostMapping("/update-password")
    public String updatePassword(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                                 BindingResult bindingResult) {
        passwordValidator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "settings";
        } else {
            logger.warn(accountDTO.toString());
            String newPassword = accountDTO.getConfirmPassword();
            String encodedPassword = passwordEncoder.encode(newPassword);
            mainService.updateUserPassword(currentAccount.getEmail(), encodedPassword);
            return "redirect:/";
        }
    }
    //TODO /change-password and enable-notifications functionality

}
