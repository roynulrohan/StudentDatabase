package com.roynulrohan.studentdatabaseapi.student;

import com.roynulrohan.studentdatabaseapi.account.Account;
import com.roynulrohan.studentdatabaseapi.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<Student> getStudents(Long accountId) {
        List<Student> students = studentRepository.findAllByAdminAccount_Id(accountId);

        if (students.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student records");
        }

        return studentRepository.findAllByAdminAccount_Id(accountId);
    }

    public Student addNewStudent(Student student, Long accountId) {
        Optional<Student> studentOptional = studentRepository.findByEmailAndAdminAccount_Id(student.getEmail(), accountId);

        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Student with email exists");
        }

        Optional<Account> accountOptional = accountRepository.findAccountById(accountId);

        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        student.setAdminAccount(accountOptional.get());

        return studentRepository.save(student);
    }
}
