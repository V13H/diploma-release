package com.hvs.diploma.entities;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.enums.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Timestamp registrationDate;
    private Timestamp lastSeen;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
    //Excluded getter for field below because Lombok generates weird getter`s names
    //so I decided to write this getters by myself
    @Getter(AccessLevel.NONE)
    private boolean hasWatchedGreetingsMessage = false;


    public static Account accountOfDTO(AccountDTO accountDTO) {
        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        account.setPhoneNumber(accountDTO.getPhoneNumber());
        account.setUserName(accountDTO.getUserName());
        return account;
    }

    public AccountDTO toDTO() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(this.email);
        accountDTO.setPassword(this.password);
        accountDTO.setPhoneNumber(this.phoneNumber);
        return accountDTO;
    }
    public boolean hasWatchedGreetingsMessage() {
        return hasWatchedGreetingsMessage;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", socialId='" + socialId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
