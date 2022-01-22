package com.roynulrohan.studentdatabaseapi.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String username) {
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(username);

        if(accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account with username does not exist.");
        }

        return accountOptional.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public void createAccount(Account account) {
        Optional<Account> studentOptional = accountRepository.findAccountByUsername(account.getUsername());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Username taken");
        }

        String password = new BCryptPasswordEncoder().encode(account.getPassword());

        account.setPassword(password);

        accountRepository.save(account);
    }
}
