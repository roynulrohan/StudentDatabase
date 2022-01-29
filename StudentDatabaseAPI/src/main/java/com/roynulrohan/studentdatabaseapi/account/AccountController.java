package com.roynulrohan.studentdatabaseapi.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.roynulrohan.studentdatabaseapi.auth.JWT.getJWTToken;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Account getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return accountService.getAccount(Long.valueOf(authentication.getPrincipal().toString()));
    }

    @PostMapping("/register")
    public Account newAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PostMapping("/login")
    public Account login(@RequestBody Account accountBody) {
        Account account = accountService.getAccount(accountBody.getUsername());

        if (!new BCryptPasswordEncoder().matches(accountBody.getPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Username / Password combination does not match.");
        }

        String token = getJWTToken(account);
        account.setToken(token);

        return account;
    }
}
