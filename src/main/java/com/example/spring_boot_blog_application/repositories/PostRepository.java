package com.example.spring_boot_blog_application.repositories;

import com.example.spring_boot_blog_application.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findAllByAccountId(Long accountId);
}
