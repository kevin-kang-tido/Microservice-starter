package com.spring.one.microstarter.feature.school.dto;

public record SchoolResponse(
        Long id,
        String schoolName,
        String schoolAddress,
        String oldName,
        String newName

) {
}
