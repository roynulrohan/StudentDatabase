package com.roynulrohan.studentdatabaseapi.account;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "account")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Account getAccount(String username) {
        return accountService.getAccount(username);
    }

    @PostMapping("/register")
    public void newAccount(@RequestBody Account account) {
        accountService.createAccount(account);
    }

    @GetMapping("/login")
    public Account login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Account account = accountService.getAccount(username);

        if (!new BCryptPasswordEncoder().matches(password, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Username / Password combination does not match.");
        }

        String token = getJWTToken(account);
        account.setToken(token);

        return account;
    }

    private String getJWTToken(Account account) {
        String secretKey = "#{systemEnvironment['JWT_SECRET']} ?: 'mySecretToken'";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder().setId(account.getId().toString()).setSubject(account.getUsername()).claim(
                "authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(
                        Collectors.toList())).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(
                new Date(System.currentTimeMillis() + 6000000)).signWith(SignatureAlgorithm.HS512,
                secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
