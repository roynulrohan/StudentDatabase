package com.roynulrohan.studentdatabaseapi;

import com.roynulrohan.studentdatabaseapi.auth.AccountRequestFilter;
import com.roynulrohan.studentdatabaseapi.auth.JWTAuthorizationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories
public class StudentDatabaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentDatabaseApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @EnableWebSecurity
    @Configuration
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().formLogin().disable().addFilterAfter(new JWTAuthorizationFilter(),
                    UsernamePasswordAuthenticationFilter.class).addFilterAfter(new AccountRequestFilter(), BasicAuthenticationFilter.class).authorizeRequests().antMatchers(
                    "/api/account/login").permitAll().antMatchers(
                    "/api/account/register").permitAll().antMatchers(
                    "/error").permitAll().antMatchers(
                    "/").permitAll().anyRequest().authenticated();
        }
    }
}
