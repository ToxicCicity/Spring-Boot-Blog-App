package com.example.spring_boot_blog_application.services;

import com.example.spring_boot_blog_application.dto.AccountDTO;
import com.example.spring_boot_blog_application.dto.AccountDTOMapper;
import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.repositories.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.spring_boot_blog_application.models.Post;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountDTOMapper accountDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountDTOMapper accountDTOMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountDTOMapper = accountDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        //account.setPassword("{bcrypt}" + account.getPassword());
        accountRepository.save(account);
    }

    public List<Post> getAllPostsOfAccount(Long id, Model model) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            List<Post> posts = account.getPosts();
            model.addAttribute("posts", posts);
            return posts;
        } else {
            return null;
        }

    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accountDTOMapper)
                .collect(Collectors.toList());
    }

    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(accountDTOMapper)
                .orElse(null);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findOneByEmail(email);

    }

}
