package com.spring.one.student.feature.student;

import com.spring.one.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    boolean existsByName(String name);

    Optional<Student> findStudentByName(String name);

    Optional<Student> findById(Long id);

    boolean existsById(Long id);
}
