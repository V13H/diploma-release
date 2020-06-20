package com.hvs.diploma.security;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.data_access_services.AccountService;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
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
    private final AccountService accountService;
    org.slf4j.Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    public CustomLogoutHandler(CurrentAccount currentAccount, AccountService accountService) {
        this.currentAccount = currentAccount;
        this.accountService = accountService;
    }

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        currentAccount.setLastSeen(LocalDateTime.now());
//        accountService.saveAccount(currentAccount.getAccountEntity());
        long id = currentAccount.getAccountEntity().getId();
        Account accountById = accountService.findAccountById(id);
        logger.warn("logout: " + accountById.toString());
        accountById.setLastSeen(Timestamp.valueOf(LocalDateTime.now()));
        accountService.saveAccount(accountById);
        request.logout();
    }
}
