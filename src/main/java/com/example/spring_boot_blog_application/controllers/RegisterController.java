package com.example.spring_boot_blog_application.controllers;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.Authority;
import com.example.spring_boot_blog_application.repositories.AuthorityRepository;
import com.example.spring_boot_blog_application.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    public RegisterController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        Account account = new Account();
        model.addAttribute("account", account);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute Account account) {
        // Save the account to the database
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
        account.setAuthorities(authorities);
        accountService.save(account);
        return "login";
    }

}
