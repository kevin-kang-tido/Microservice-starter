package com.spring.one.student.feature.student;

import com.spring.one.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    boolean existsByName(String name);
}
