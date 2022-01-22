package com.roynulrohan.studentdatabaseapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(Long adminId) {
        return studentRepository.findStudentsByAdminAccount_Id(adminId).stream().toList();
    }

    public Student getStudent(String email) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);

        if(studentOptional.isEmpty()) {
            throw new IllegalStateException("Student with that email does not exist.");
        }

        return studentOptional.get();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Student with email exists");
        }

        studentRepository.save(student);
    }
}
