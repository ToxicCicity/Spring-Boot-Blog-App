package com.example.spring_boot_blog_application.services;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.repositories.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        //account.setPassword("{bcrypt}" + account.getPassword());
        accountRepository.save(account);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }
}
