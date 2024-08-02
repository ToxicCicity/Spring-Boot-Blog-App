package com.example.spring_boot_blog_application.controllers;

import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.services.PostService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    //home page
    @GetMapping("/")
    public List<Post> home(Model model) {
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);
        return posts;
    }

}
