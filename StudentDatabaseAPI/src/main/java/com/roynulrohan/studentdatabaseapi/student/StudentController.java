package com.roynulrohan.studentdatabaseapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/{accountId}/students")
@Validated
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents(@PathVariable("accountId") Long accountId) {
        return studentService.getStudents(accountId);
    }

    @GetMapping("{studentId}")
    public Student getStudent(@PathVariable("accountId") Long accountId, @PathVariable("studentId") Long studentId) {
        return studentService.getStudent(accountId, studentId);
    }

    @PostMapping
    public Student registerNewStudent(@RequestBody Student student, @PathVariable("accountId") Long accountId) {
        return studentService.addNewStudent(student, accountId);
    }

    @DeleteMapping("{studentId}")
    public void deleteStudent(@PathVariable("accountId") Long accountId, @PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(accountId, studentId);
    }

    @PutMapping("{studentId}")
    public Student replaceStudent(@RequestBody Student student, @PathVariable("accountId") Long accountId, @PathVariable("studentId") Long studentId) {
        return studentService.replaceStudent(student, accountId, studentId);
    }

    @PatchMapping("{studentId}")
    public Student updateStudent(@PathVariable("accountId") Long accountId,@PathVariable("studentId") Long studentId, @RequestParam(required = false) String name, @RequestParam(required = false) String email) {
        return studentService.updateStudent(accountId, studentId, name, email);
    }
}
