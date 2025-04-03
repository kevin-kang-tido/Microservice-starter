package com.spring.one.student.feature.student;

import com.spring.one.student.feature.student.dto.StudentRequest;
import com.spring.one.student.feature.student.dto.StudentResponse;
import org.springframework.data.domain.Page;

public interface StudentService {

    StudentResponse createStudent(StudentRequest studentRequest);

    // TODO: get all Student list
    Page<StudentResponse> getAllStudentList(int page, int size);


}
