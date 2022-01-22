package com.roynulrohan.studentdatabaseapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping
@Validated
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/account/{accountId}/students")
    public List<Student> getStudents(@PathVariable(value = "accountId") Long accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!accountId.equals(Long.valueOf(authentication.getPrincipal().toString()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return studentService.getStudents(accountId);
    }

    @PostMapping("/account/{accountId}/students")
    public Student registerNewStudent(@RequestBody Student student, @PathVariable(value = "accountId") Long accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!accountId.equals(Long.valueOf(authentication.getPrincipal().toString()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return studentService.addNewStudent(student, accountId);
    }
}
