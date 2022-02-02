package com.roynulrohan.studentdatabaseapi.auth;

import com.roynulrohan.studentdatabaseapi.account.Account;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWT {
    static String secretKey = Dotenv.load().get("JWT_SECRET");

    public static String getJWTToken(Account account) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder().setId(account.getId().toString()).setSubject(account.getUsername()).claim("authorities",
                grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(
                new Date(System.currentTimeMillis() + Duration.ofHours(5).toMillis())).signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
