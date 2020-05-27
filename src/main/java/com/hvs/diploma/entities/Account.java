package com.hvs.diploma.entities;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String socialId;
    private String pictureUrl = "/img/anonymous-user-svg.svg";
    private String email;
    private String password;
    private String phoneNumber;
    private boolean hasWatchedGreetingsMessage;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
//    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL)
//    private AccountSettings settings = new AccountSettings();


    public static Account accountOfDto(AccountDTO accountDTO) {
        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        account.setPhoneNumber(accountDTO.getPhoneNumber());
        account.setUserName(accountDTO.getUserName());
        return account;
    }

}
