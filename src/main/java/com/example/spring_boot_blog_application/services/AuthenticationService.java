package com.example.spring_boot_blog_application.services;

import com.example.spring_boot_blog_application.models.Account;
import com.example.spring_boot_blog_application.models.AuthenticationRequest;
import com.example.spring_boot_blog_application.models.AuthenticationResponse;
import com.example.spring_boot_blog_application.models.RegistrationRequest;
import com.example.spring_boot_blog_application.repositories.AccountRepository;
import com.example.spring_boot_blog_application.repositories.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) {
        var userAuthority = authorityRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Authority: ROLE_USER not initialized"));
        var user = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .authority(List.of(userAuthority))
                .build();
        accountRepository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var claims = new HashMap<String, Object>();
        var user = ((Account)auth.getPrincipal());
        claims.put("fullName", user.getFullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

}
