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
        return studentRepository.findAllByAdminAccount_Id(accountId);
    }

    public Student getStudent(String email) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);

        if (studentOptional.isEmpty()) {
            throw new IllegalStateException("Student with that email does not exist.");
        }

        return studentOptional.get();
    }

    public Student addNewStudent(Student student, Long accountId) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Student with email exists");
        }

        Optional<Account> accountOptional = accountRepository.findAccountById(accountId);

        if(accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        student.setAdminAccount(accountOptional.get());

        return studentRepository.save(student);
    }
}
