package com.hvs.diploma.security;

import com.hvs.diploma.dao.main.AccountRepository;
import com.hvs.diploma.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository repository;

    @Autowired
    public UserDetailsServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account account = repository.findAccountByEmail(s);
        if (account == null) {
            throw new UsernameNotFoundException("User with specified email doesn`t exists");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new User(account.getEmail(), account.getPassword(), authorities);
    }
}
