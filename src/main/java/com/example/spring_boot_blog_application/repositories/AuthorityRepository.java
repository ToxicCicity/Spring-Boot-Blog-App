package com.example.spring_boot_blog_application.repositories;

import com.example.spring_boot_blog_application.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String>{
    Optional<Authority> findByName(String name);
}
