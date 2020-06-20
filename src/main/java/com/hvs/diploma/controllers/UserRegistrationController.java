package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.validation_services.form_validators.RegistrationFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//DONE
@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    private final CurrentAccount currentAccount;
    private final RegistrationFormValidator validator;
    private final BCryptPasswordEncoder encoder;
    private final MainService mainService;
    Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    @Autowired
    public UserRegistrationController(MainService mainService, BCryptPasswordEncoder encoder, RegistrationFormValidator validator, CurrentAccount currentAccount) {
        this.mainService = mainService;
        this.encoder = encoder;
        this.validator = validator;
        this.currentAccount = currentAccount;
    }

    @GetMapping()
    public String getRegistrationPage(Model model) {
        model.addAttribute("accountDTO", new AccountDTO());
        return "register";
    }

    @PostMapping("/createAccount")
    public String registerAccount(@Valid @ModelAttribute("accountDTO") AccountDTO accountDTO,
                                  BindingResult bindingResult, HttpServletRequest request,
                                  Model model) throws ServletException, IOException {
        validator.validate(accountDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            Account account = Account.accountOfDTO(accountDTO);
            account.setPassword(encoder.encode(accountDTO.getPassword()));
            account.setPictureUrl("/img/anonymous-user-svg.svg");
            account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
            mainService.saveAccount(account);
            model.addAttribute("account", account);
            currentAccount.setAccountEntity(account);
            logger.warn(currentAccount.toString());
            request.login(account.getEmail(), accountDTO.getPassword());
            return "redirect:/";
        }
    }
}
