package com.example.spring_boot_blog_application.config;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.Authority;
import com.example.spring_boot_blog_application.models.Post;
import com.example.spring_boot_blog_application.repositories.AuthorityRepository;
import com.example.spring_boot_blog_application.services.AccountService;
import com.example.spring_boot_blog_application.services.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeedData implements CommandLineRunner { //uncomment this line to enable the old method to seed data

    private final PostService postService;

    private final AccountService accountService;

    private final AuthorityRepository authorityRepository;

    public SeedData(PostService postService, AccountService accountService, AuthorityRepository authorityRepository) {
        this.postService = postService;
        this.accountService = accountService;
        this.authorityRepository = authorityRepository;
    }

    // old method to seed data
//    // This method will run when the application starts and will create some seed data if there is no data in the database
    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        if (authorityRepository.findByName("ROLE_USER").isEmpty()) {
				authorityRepository.save(
						Authority.builder()
								.name("ROLE_USER")
								.build()
				);
        }

        if (authorityRepository.findByName("ROLE_ADMIN").isEmpty()) {
            authorityRepository.save(
                    Authority.builder()
                            .name("ROLE_ADMIN")
                            .build()
            );
        }

        if(posts.isEmpty()) {

//            Authority user = new Authority();
//            user.setName("ROLE_USER");
//            authorityRepository.save(user);
//
//            Authority admin = new Authority();
//            admin.setName("ROLE_ADMIN");
//            authorityRepository.save(admin);

            Account account1 = new Account();
            Account account2 = new Account();

            account1.setFirstName("user");
            account1.setLastName("user");
            account1.setEmail("user.user@domain.com");
            account1.setPassword("password");
            account1.setAuthority(List.of(authorityRepository.findByName("ROLE_USER").get()));
            account1.setEnabled(true);


            account2.setFirstName("admin");
            account2.setLastName("admin");
            account2.setEmail("admin.admin@domain.com");
            account2.setPassword("password");
            account2.setAuthority(List.of(authorityRepository.findByName("ROLE_USER").get()));
            account2.setAuthority(List.of(authorityRepository.findByName("ROLE_ADMIN").get()));
            account2.setEnabled(true);

            accountService.save(account1);
            accountService.save(account2);

            Post post1 = new Post();
            post1.setTitle("First Post");
            post1.setBody("This is the body of the first post");
            post1.setAccount(account1);
            postService.save(post1);

            Post post2 = new Post();
            post2.setTitle("Second Post");
            post2.setBody("This is the body of the second post");
            post2.setAccount(account2);
            postService.save(post2);
        }

    }

}
