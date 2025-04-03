package com.spring.one.microstarter.feature.school;

import com.spring.one.microstarter.domain.School;
import com.spring.one.microstarter.feature.school.dto.SchoolRequest;
import com.spring.one.microstarter.feature.school.dto.SchoolResponse;
import org.springframework.data.domain.Page;

public interface SchoolService {


    // TODO: create school
    SchoolResponse create(SchoolRequest schoolRequest);

    // TODO: all the or find by list school
    Page<SchoolResponse> getSchoolList(int page, int size);

    // TODO: find school by name
    SchoolResponse findSchoolByName(String name);

    // TODO: findSchool by BY ID
    SchoolResponse findSchoolByID(Long id);

    // TODO: delete by Id
    void deleteSchoolById(Long id);

    // TODO: Update school by Id
    SchoolResponse updateSchoolById(Long id, SchoolRequest schoolRequest);

}
