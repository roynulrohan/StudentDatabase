package com.roynulrohan.studentdatabaseapi.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmailAndAdminAccount_Id(String email, Long id);

    List<Student> findAllByAdminAccount_Id(Long id);
}
