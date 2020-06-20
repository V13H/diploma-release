package com.hvs.diploma.security;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.data_access_services.MainService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class CustomLogoutHandler implements LogoutHandler {
    private final CurrentAccount currentAccount;
    private final MainService mainService;

    @Autowired
    public CustomLogoutHandler(CurrentAccount currentAccount, MainService mainService) {
        this.currentAccount = currentAccount;
        this.mainService = mainService;
    }

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        currentAccount.setLastSeen(LocalDateTime.now());
//        accountService.saveAccount(currentAccount.getAccountEntity());
        long id = currentAccount.getAccountEntity().getId();
        Account accountById = mainService.findAccountById(id);
        accountById.setLastSeen(Timestamp.valueOf(LocalDateTime.now()));
        mainService.saveAccount(accountById);
        request.logout();
    }
}
