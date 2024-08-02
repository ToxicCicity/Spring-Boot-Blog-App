package com.example.spring_boot_blog_application.services;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountService accountservice;

    public PostService(PostRepository postRepository, AccountService accountservice) {
        this.postRepository = postRepository;
        this.accountservice = accountservice;
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

//    public List<Post> getAllByAccountId(Long accountId) {
//        return postRepository.findAllByAccountId(accountId);
//    }

    public Post getPost(Long id, Model model) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return post;
        } else {
            return null;
        }
    }

    public Post createNewPost(Post post, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Account account = accountservice.findByEmail(authUsername).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        post.setAccount(account);
        save(post);
        return post;
    }

    public Post getPostForEdit(Long id, Model model) {
        // find post by id
        Optional<Post> optionalPost = getById(id);
        // if post exist put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return post;
        } else {
            return null;
        }
    }

    public Post updatePost(Long id,Post post) {
        Optional<Post> optionalPost = getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            save(existingPost);
            return existingPost;
        }
        return null;
    }

    public String deletePost(Long id) {
        // find post by id
        Optional<Post> optionalPost = getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            delete(post);
            return "post deleted";
        } else {
            return "404 post not found";
        }
    }

    public void save(Post post) {
        if(post.getId() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

}
