package com.spring.one.student.feature.student.dto;

public record StudentResponse(
        Long id,
        String name,
        int age,
        String gender,
        Integer schoolId

) {
}
