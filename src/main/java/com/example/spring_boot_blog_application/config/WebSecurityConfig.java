package com.example.spring_boot_blog_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//spring security 6 ya göre yapıyorum, tutorialdekileri değiştirdim
//https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#migrate-authorize-requests
//https://www.youtube.com/watch?v=cwcfHwA9-nw
//https://stackoverflow.com/questions/77087315/cant-resolve-some-of-the-deprecated-methods-of-spring-security-like-and-and-f

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static final String[] WHITELIST = {
        "/register",
        "/h2-console/*",
        "/"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers(WHITELIST).permitAll()
                    .requestMatchers(HttpMethod.GET,"/posts/*").permitAll()
                    .anyRequest().authenticated()
            );

        http
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error=true")
                    .permitAll()
            );

        http.logout(lOut -> {
            lOut
                    //.invalidateHttpSession(true)
                    //.clearAuthentication(true)
                    //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
        });

        http.httpBasic(Customizer.withDefaults());

        // TODO: h2-console'dan ayrılınca bunları sil
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);
        //http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)); bir üstteki sanki daha iyi oldu
        return http.build();
    }

}
