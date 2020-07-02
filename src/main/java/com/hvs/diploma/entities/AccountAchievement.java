package com.hvs.diploma.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class AccountAchievement {
    @EmbeddedId
    private AccountAchievementId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("achievementId")
    private Achievement achievement;

    public AccountAchievement(Account account, Achievement achievement) {
        this.account = account;
        this.achievement = achievement;
        this.id = new AccountAchievementId(account.getId(), achievement.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountAchievement)) return false;
        AccountAchievement that = (AccountAchievement) o;
        return Objects.equals(account, that.account) &&
                Objects.equals(achievement, that.achievement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, achievement);
    }

    @Override
    public String toString() {
        return "AccountAchievement{" +
                "id=" + id +
                '}';
    }
}
