package com.hvs.diploma.controllers;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.pojo.CurrentAccount;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.validators.RegistrationFormValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

//DONE
@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    private final CurrentAccount currentAccount;
    private final RegistrationFormValidator validator;
    private final BCryptPasswordEncoder encoder;
    private final MainService mainService;
    org.slf4j.Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);
//    private final TurboSmsMessageRepository repository;

    @Autowired
    public UserRegistrationController(MainService mainService, BCryptPasswordEncoder encoder, RegistrationFormValidator validator, CurrentAccount currentAccount) {
        this.mainService = mainService;
        this.encoder = encoder;
        this.validator = validator;
        this.currentAccount = currentAccount;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
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
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            Account account = Account.accountOfDTO(accountDTO);
            account.setPassword(encoder.encode(accountDTO.getPassword()));
            account.setPictureUrl("/img/anonymous-user-svg.svg");
            mainService.saveAccount(account);
            model.addAttribute("account", account);
            request.login(account.getEmail(), accountDTO.getPassword());
            currentAccount.setAccount(account);
            return "redirect:/";
        }
    }
}
