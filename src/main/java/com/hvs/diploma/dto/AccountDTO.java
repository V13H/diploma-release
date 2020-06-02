package com.hvs.diploma.dto;

import com.hvs.diploma.entities.Account;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.HashMap;
import java.util.Map;

@Data
@Getter
@Setter
@ToString
public class AccountDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String userName;
    org.slf4j.Logger logger = LoggerFactory.getLogger(AccountDTO.class);

    public Account getAccount(OAuth2AuthenticationToken token) {
        Account account = new Account();
        switch (token.getAuthorizedClientRegistrationId()) {
            case "facebook":
                account = getFacebookAuthBasedAccount(token.getPrincipal().getAttributes());
                break;
            case "google":
                account = getGoogleAuthBasedAccount(token.getPrincipal().getAttributes());
                break;
            case "github":
                account = getGithubAuthBasedAccount(token.getPrincipal().getAttributes());
                break;
        }
        return account;
    }


    private Account getFacebookAuthBasedAccount(Map<String, Object> userAttributes) {

        Account account = new Account();
        account.setUserName(userAttributes.get("name").toString().split(" ")[0]);
        Map<String, Object> picParams = (Map<String, Object>) ((HashMap) userAttributes.get("picture")).get("data");
        String picUrl = (String) picParams.get("url");
        account.setPictureUrl(picUrl);
        account.setSocialId((String) userAttributes.get("id"));

        return account;
    }

    private Account getGithubAuthBasedAccount(Map<String, Object> userAttributes) {
        Account account = new Account();
        if (userAttributes != null) {
            String name = (String) userAttributes.get("name");
            account.setUserName(name);
            String avatar_url = (String) userAttributes.get("avatar_url");
            account.setPictureUrl(avatar_url);
            String socialId = userAttributes.get("id").toString();
            account.setSocialId(socialId);
            if (userAttributes.get("email") != null) {
                account.setEmail(userAttributes.get("email").toString());
            }
        }
        return account;
    }

    private Account getGoogleAuthBasedAccount(Map<String, Object> userAttributes) {
        Account account = new Account();
        account.setUserName((String) userAttributes.get("given_name"));
        account.setPictureUrl((String) userAttributes.get("picture"));
        account.setSocialId((String) userAttributes.get("sub"));
        account.setEmail(userAttributes.get("email").toString());
        return account;
    }
}
