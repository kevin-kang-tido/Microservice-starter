package com.spring.one.student.mapper;


import com.spring.one.student.domain.Student;
import com.spring.one.student.feature.student.dto.StudentRequest;
import com.spring.one.student.feature.student.dto.StudentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // to reponse
    Student formStudentCreateRequest(StudentRequest studentRequest);

    // to domain
    StudentResponse toStudentResponse(Student student);

    // update  student
    @Mapping(target = "id", ignore = true)
    void updateStudentRequest(StudentRequest studentRequest,@MappingTarget Student student);

}
