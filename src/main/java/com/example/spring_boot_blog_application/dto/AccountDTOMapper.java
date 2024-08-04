package com.example.spring_boot_blog_application.dto;

import com.example.spring_boot_blog_application.models.Account;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AccountDTOMapper implements Function<Account, AccountDTO> {

        @Override
        public AccountDTO apply(Account account) {
            return new AccountDTO(account.getEmail(), account.getFirstName(), account.getLastName());
        }

}
