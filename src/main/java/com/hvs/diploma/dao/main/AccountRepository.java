package com.hvs.diploma.dao.main;

import com.hvs.diploma.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountBySocialId(String socialID);

    Account findAccountByEmail(String email);
}
