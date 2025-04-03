package com.spring.one.microstarter.mapper;


import com.spring.one.microstarter.domain.School;
import com.spring.one.microstarter.feature.school.dto.SchoolRequest;
import com.spring.one.microstarter.feature.school.dto.SchoolResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.transaction.reactive.TransactionalOperator;

@Mapper(componentModel = "spring")
public interface SchoolMapper {

    // to domain
    School formSchoolCreateRequest(SchoolRequest schoolRequest);

    SchoolResponse toSchoolResponse(School school);

    // TODO: mapper to update the School

    @Mapping(target = "id",ignore = true)
    void updateSchoolFormRequest(SchoolRequest schoolRequest,  @MappingTarget School school);


}
