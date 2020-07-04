package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.components.CurrentUser;
import com.hvs.diploma.dao.main.AccountRepository;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public Account findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account findAccountBySocialId(String socialId) {
        return accountRepository.findAccountBySocialId(socialId);
    }


    @Transactional(readOnly = true)
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void updateAccount(CurrentUser currentUser) {
        saveAccount(currentUser.getAccount());
    }

    public Account findAccountByPhone(String phoneNumber) {
        return accountRepository.findAccountByPhoneNumber(phoneNumber);
    }

    public long getUsersCount() {
        return accountRepository.countAccountsByRole(UserRole.ROLE_COMMON_USER);
    }

    public List<Account> findUserAccounts(Pageable pageable) {
        return accountRepository.findAccountsByRoleIsNot(UserRole.ROLE_ADMIN, pageable);
    }
    public Account findAccountById(long id) {
        return accountRepository.findAccountById(id);
    }
}
