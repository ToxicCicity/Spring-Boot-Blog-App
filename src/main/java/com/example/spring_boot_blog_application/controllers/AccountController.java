package com.example.spring_boot_blog_application.controllers;

import com.example.spring_boot_blog_application.dto.AccountDTO;
import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.services.AccountService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //old account profile page method, replaced with the one below
//    @GetMapping("/account/{id}/posts")
//     public List<Post> getAccount(@PathVariable Long id, Model model) {
//         Optional<Account> optionalAccount = accountService.getById(id);
//         if(optionalAccount.isPresent()) {
//             Account account = optionalAccount.get();
//             model.addAttribute("account", account);
//             List<Post> posts = postService.getAllByAccountId(id);
//             model.addAttribute("posts", posts);
//             return posts;
//         } else {
//                return null;
//         }
//     }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    //gets endpoint /account/{id}/posts and returns all posts by the account with the given id
    @GetMapping("/{id}/posts")
    public List<Post> getAccount(@PathVariable Long id, Model model) {
        return accountService.getAllPostsOfAccount(id, model);
    }

}
