package com.hvs.diploma.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
public class AccountAchievementId implements Serializable {
    private Long accountId;
    private Long achievementId;

    private AccountAchievementId() {
    }

    public AccountAchievementId(Long accountId, Long achievementId) {
        this.accountId = accountId;
        this.achievementId = achievementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountAchievementId)) return false;
        AccountAchievementId that = (AccountAchievementId) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(achievementId, that.achievementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, achievementId);
    }
}
