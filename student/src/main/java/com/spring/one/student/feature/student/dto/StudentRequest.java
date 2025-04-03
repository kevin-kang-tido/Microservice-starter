package com.spring.one.student.feature.student.dto;

public record StudentRequest(
        String name,
        int age,
        String gender,
        Integer schoolId

) {
}
