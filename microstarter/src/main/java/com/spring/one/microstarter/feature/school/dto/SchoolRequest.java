package com.spring.one.microstarter.feature.school.dto;
import lombok.Builder;

@Builder
public record SchoolRequest(

    String schoolName,

    String schoolAddress,

    String oldName,

    String newName
) {
}
