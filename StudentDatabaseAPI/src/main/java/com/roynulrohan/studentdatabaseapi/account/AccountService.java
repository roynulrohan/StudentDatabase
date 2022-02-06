package com.roynulrohan.studentdatabaseapi.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.roynulrohan.studentdatabaseapi.authorization.JWT.getJWTToken;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String username) {
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(username);

        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Username / Password combination does not match.");
        }

        return accountOptional.get();
    }

    public Account getAccount(Long userId) {
        Optional<Account> accountOptional = accountRepository.findAccountById(userId);

        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with username does not exist.");
        }

        return accountOptional.get();
    }

    public Account createAccount(Account account) {
        Optional<Account> studentOptional = accountRepository.findAccountByUsername(account.getUsername());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Account with username already exists.");
        }

        String password = new BCryptPasswordEncoder().encode(account.getPassword());

        account.setPassword(password);

        accountRepository.save(account);

        String token = getJWTToken(account);
        account.setToken(token);

        return account;
    }
}
