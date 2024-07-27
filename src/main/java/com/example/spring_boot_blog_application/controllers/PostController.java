package com.example.spring_boot_blog_application.controllers;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.services.AccountService;
import com.example.spring_boot_blog_application.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
public class PostController {

    private final PostService postService;

    private final AccountService accountService;

    public PostController(PostService postService, AccountService accountService) {
        this.postService = postService;
        this.accountService = accountService;
    }

    //returns a post by id
    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);
        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return post;
        } else {
            return null;
        }
    }

    //old createNewPost and saveNewPost methods, replaced with the ones below (was causing every post to be created by the same user)
//    @GetMapping("/posts/new")
//    public String createNewPost(Model model) {
//        Optional<Account> optionalAccount = accountService.findByEmail("user.user@domain.com");
//        if(optionalAccount.isPresent()) {
//            Account account = optionalAccount.get();
//            Post post = new Post();
//            post.setAccount(account);
//            model.addAttribute("post", post);
//            return "post_new";
//        } else {
//            return "404";
//        }
//    }
//
//    @PostMapping("/posts/new")
//    public String saveNewPost(@ModelAttribute Post post) {
//        postService.save(post);
//        return "redirect:/posts/" + post.getId();
//    }

    //gets endpoint /posts/new and returns a new post object
    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public Post createNewPost(Model model) {

        Post post = new Post();
        model.addAttribute("post", post);
        return post;
    }

    //posts endpoint /posts/new and creates a new post bound to the authenticated user
    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public Post createNewPost(@ModelAttribute Post post, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Account account = accountService.findByEmail(authUsername).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        post.setAccount(account);
        postService.save(post);
        return post;
    }

    //gets endpoint /posts/{id}/edit and returns a post object for editing by the authenticated user
    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @postService.getById(#id).get().getAccount().getEmail() == principal.username")
    public Post getPostForEdit(@PathVariable Long id, Model model) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        // if post exist put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return post;
        } else {
            return null;
        }
    }

    //posts endpoint /posts/{id} and updates the post with the new data
    @PostMapping("/posts/{id}")
    @PreAuthorize(" hasRole('ROLE_ADMIN') or @postService.getById(#id).get().getAccount().getEmail() == principal.username")
    public Post updatePost(@PathVariable Long id, Post post, BindingResult bindingResult, Model model) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            postService.save(existingPost);
            return existingPost;
        }
        return null;
    }

    //gets endpoint /posts/{id}/delete and deletes the post if the authenticated user is the owner or an admin
    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @postService.getById(#id).get().getAccount().getEmail() == principal.username")
    public String deletePost(@PathVariable Long id) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            postService.delete(post);
            return "post deleted";
        } else {
            return "404 post not found";
        }
    }


}
