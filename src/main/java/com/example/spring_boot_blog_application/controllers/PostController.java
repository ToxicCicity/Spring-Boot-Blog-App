package com.example.spring_boot_blog_application.controllers;

import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.services.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //returns a post by id
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id, Model model) {
        return postService.getPost(id, model);
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
    @GetMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public Post createNewPost(Model model) {

        Post post = new Post();
        model.addAttribute("post", post);
        return post;
    }

    //posts endpoint /posts/new and creates a new post bound to the authenticated user
    @PostMapping("/new")
    @PreAuthorize("isAuthenticated()")
    public Post createNewPost(@ModelAttribute Post post, Principal principal) {
        return postService.createNewPost(post, principal);
    }

    //gets endpoint /posts/{id}/edit and returns a post object for editing by the authenticated user
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @postService.getById(#id).get().getAccount().getEmail() == principal.username")
    public Post getPostForEdit(@PathVariable Long id, Model model) {
        return postService.getPostForEdit(id, model);
    }

    //posts endpoint /posts/{id} and updates the post with the new data
    @PostMapping("/{id}")
    @PreAuthorize(" hasRole('ROLE_ADMIN') or @postService.getById(#id).get().getAccount().getEmail() == principal.username")
    public Post updatePost(@PathVariable Long id, Post post, BindingResult bindingResult, Model model) {
        return postService.updatePost(id, post);
    }

    //gets endpoint /posts/{id}/delete and deletes the post if the authenticated user is the owner or an admin
    @GetMapping("/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @postService.getById(#id).get().getAccount().getEmail() == principal.username")
    public String deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }


}
