package com.hvs.diploma.security;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.UserRole;
import com.hvs.diploma.services.data_access_services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MainService mainService;
    private final CurrentAccount currentAccount;

    @Autowired
    public OAuth2SuccessHandler(MainService mainService, CurrentAccount currentAccount) {
        this.mainService = mainService;
        this.currentAccount = currentAccount;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        Account account = AccountDTO.getAccount(token);
        String socialId = account.getSocialId();
        Account accountBySocialId = mainService.findAccountBySocialId(socialId);
        if (accountBySocialId == null) {
            mainService.saveAccount(account);

        } else {
            account = accountBySocialId;
        }
        account.setRole(UserRole.ROLE_COMMON_USER);
        currentAccount.setAccountEntity(account);
        httpServletResponse.sendRedirect("/");
    }
}
