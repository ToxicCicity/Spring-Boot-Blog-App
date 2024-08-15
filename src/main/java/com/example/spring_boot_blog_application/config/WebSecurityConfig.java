package com.example.spring_boot_blog_application.config;

import com.example.spring_boot_blog_application.services.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

//spring security 6 ya göre yapıyorum, tutorialdekileri değiştirdim
//https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#migrate-authorize-requests
//https://www.youtube.com/watch?v=cwcfHwA9-nw
//https://stackoverflow.com/questions/77087315/cant-resolve-some-of-the-deprecated-methods-of-spring-security-like-and-and-f

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

//    @Bean
//    public static PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;

    public WebSecurityConfig(AuthenticationProvider authenticationProvider, JwtFilter jwtAuthFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    private static final String[] WHITELIST = {
        "/register",
        "/h2-console/*",
        "/",
        "/account*",
        "/auth/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.cors(withDefaults())
                //.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers(WHITELIST).permitAll()
                    .requestMatchers(HttpMethod.GET,"/posts/*").permitAll()
                    .anyRequest().authenticated()
            )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic(withDefaults());

        // TODO: h2-console'dan ayrılınca bunları sil (niye yazmisim ki?)
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(AbstractHttpConfigurer::disable);
        //http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)); bir üstteki sanki daha iyi oldu
        return http.build();
    }

}
