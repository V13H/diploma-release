package com.hvs.diploma.entities;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.enums.UserRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
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
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccountAchievement> accountAchievements = new HashSet<>();

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

    public void addAchievement(Achievement achievement) {
        AccountAchievement accountAchievement = new AccountAchievement(this, achievement);
        accountAchievements.add(accountAchievement);
//        achievement.getAccounts().add(accountAchievement);
    }

    public List<Achievement> getAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        if (accountAchievements != null) {
            for (AccountAchievement accountAchievement : accountAchievements) {
                achievements.add(accountAchievement.getAchievement());
            }
            return achievements;
        } else {
            return null;
        }
    }
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", achievements=" + accountAchievements +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
