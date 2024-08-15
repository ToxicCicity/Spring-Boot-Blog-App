package com.example.spring_boot_blog_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBlogApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner runner(AuthorityRepository authorityRepository){
//		return args -> {
//			if (authorityRepository.findByName("ROLE_USER").isEmpty()) {
//				authorityRepository.save(
//						Authority.builder()
//								.name("ROLE_USER")
//								.build()
//				);
//			}
//		};
//	}

}
