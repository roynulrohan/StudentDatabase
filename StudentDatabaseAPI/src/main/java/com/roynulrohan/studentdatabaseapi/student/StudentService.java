package com.roynulrohan.studentdatabaseapi.student;

import com.roynulrohan.studentdatabaseapi.account.Account;
import com.roynulrohan.studentdatabaseapi.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AccountRepository accountRepository;

    private void verifyAccount(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findAccountById(accountId);

        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account does not exist");
        }
    }

    public List<Student> getStudents(Long accountId) {
        verifyAccount(accountId);

        List<Student> students = studentRepository.findAllByAdminAccount_Id(accountId);

        if (students.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student records");
        }

        return studentRepository.findAllByAdminAccount_Id(accountId);
    }

    public Student getStudent(Long accountId, Long studentId) {
        verifyAccount(accountId);

        Optional<Student> student = studentRepository.findByIdAndAdminAccount_Id(studentId, accountId);

        if (student.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with ID %d not found.", studentId));
        }

        return student.get();
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

    public void deleteStudent(Long accountId, Long studentId) {
        verifyAccount(accountId);

        Optional<Student> student = studentRepository.findByIdAndAdminAccount_Id(studentId, accountId);

        if (student.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with ID %d not found.", studentId));
        }

        studentRepository.delete(student.get());
    }

    public Student replaceStudent(Student student, Long accountId, Long studentId) {
        verifyAccount(accountId);

        Optional<Student> studentOptional = studentRepository.findByIdAndAdminAccount_Id(studentId, accountId);

        if (studentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with ID %d not found.", studentId));
        }

        boolean emailExists = studentRepository.findByEmailAndAdminAccount_Id(student.getEmail(), accountId).filter(student1 -> !Objects.equals(student1.getId(), studentId)).isPresent();

        if (emailExists) {
            throw new IllegalStateException("Student with email exists");
        }

        Optional<Account> accountOptional = accountRepository.findAccountById(accountId);

        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        student.setAdminAccount(accountOptional.get());
        student.setId(studentId);

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long accountId, Long studentId, String name, String email) {
        Optional<Student> studentOptional = studentRepository.findByIdAndAdminAccount_Id(studentId, accountId);

        if (studentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with ID %d not found.", studentId));
        }

        boolean emailExists = studentRepository.findByEmailAndAdminAccount_Id(email, accountId).filter(student1 -> !Objects.equals(student1.getId(), studentId)).isPresent();

        if (emailExists) {
            throw new IllegalStateException("Student with email exists");
        }

        Optional<Account> accountOptional = accountRepository.findAccountById(accountId);

        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if(name != null && name.length() > 0 && !Objects.equals(studentOptional.get().getName(),name)) {
            studentOptional.get().setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(studentOptional.get().getEmail(),email)) {
            studentOptional.get().setEmail(email);
        }

        return studentRepository.save(studentOptional.get());
    }
}
