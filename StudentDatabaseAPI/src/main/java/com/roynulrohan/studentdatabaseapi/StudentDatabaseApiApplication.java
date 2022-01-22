package com.roynulrohan.studentdatabaseapi;

import com.roynulrohan.studentdatabaseapi.account.AccountRepository;
import com.roynulrohan.studentdatabaseapi.auth.JWTAuthorizationFilter;
import com.roynulrohan.studentdatabaseapi.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@RestController
@EnableJpaRepositories
public class StudentDatabaseApiApplication implements CommandLineRunner {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(StudentDatabaseApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @RequestMapping("hello")
    public String getHello() {
        return "Hello Spring!";
    }

    @Override
    public void run(String... args) throws Exception {
//        Account roynul = new Account("Roynul", new BCryptPasswordEncoder().encode("123"));
//
//        accountRepository.saveAll(List.of(roynul, slay));
//
//        Student roynul1 = new Student("Roynul", "roynulrohan@gmail.com", LocalDate.of(2002, 6, 30), roynul);
//
//        studentRepository.saveAll(List.of(roynul1, slay1));
    }

    @EnableWebSecurity
    @Configuration
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable().formLogin().disable().addFilterAfter(new JWTAuthorizationFilter(),
                    UsernamePasswordAuthenticationFilter.class).authorizeRequests().antMatchers(
                    "/account/login").permitAll().antMatchers(
                    "/account/register").permitAll().antMatchers(
                    "/error").permitAll().anyRequest().authenticated();
        }
    }
}
