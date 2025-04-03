package com.spring.one.microstarter.feature.school;

import com.spring.one.microstarter.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School,Long> {

    boolean existsBySchoolName(String schoolName);

    Optional<School> findBySchoolName(String schoolName);

    Optional<School> findById(Long id);

}
