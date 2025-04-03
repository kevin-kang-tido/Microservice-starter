package com.spring.one.student.mapper;


import com.spring.one.student.domain.Student;
import com.spring.one.student.feature.student.dto.StudentRequest;
import com.spring.one.student.feature.student.dto.StudentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // to reponse
    Student formStudentCreateRequest(StudentRequest studentRequest);

    // to domain
    StudentResponse toStudentResponse(Student student);

}
