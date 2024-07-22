package com.example.spring_boot_blog_application.controllers;


import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.services.AccountService;
import com.example.spring_boot_blog_application.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PostService postService;

     @GetMapping("/account/{id}/posts")
     public String getAccount(@PathVariable Long id, Model model) {
         Optional<Account> optionalAccount = accountService.getById(id);
         if(optionalAccount.isPresent()) {
             Account account = optionalAccount.get();
             model.addAttribute("account", account);
             List<Post> posts = postService.getAllByAccountId(id);
             model.addAttribute("posts", posts);
             return "account";
         } else {
             return "404";
         }
     }


}
