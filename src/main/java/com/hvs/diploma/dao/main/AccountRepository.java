package com.hvs.diploma.dao.main;

import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountBySocialId(String socialID);

    Account findAccountByEmail(String email);

    Account findAccountByPhoneNumber(String phoneNumber);

    long countAccountsByRole(UserRole role);

    List<Account> findAccountsByRoleIsNot(UserRole role, Pageable pageable);
}
