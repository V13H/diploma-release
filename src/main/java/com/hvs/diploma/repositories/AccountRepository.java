package com.hvs.diploma.repositories;

import com.hvs.diploma.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountBySocialId(String socialID);

    Account findAccountByEmail(String email);
}
