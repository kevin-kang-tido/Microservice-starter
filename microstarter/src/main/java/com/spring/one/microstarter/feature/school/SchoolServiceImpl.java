package com.spring.one.microstarter.feature.school;

import com.spring.one.microstarter.domain.School;
import com.spring.one.microstarter.feature.school.dto.SchoolRequest;
import com.spring.one.microstarter.feature.school.dto.SchoolResponse;
import com.spring.one.microstarter.mapper.SchoolMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;

    @Override
    public SchoolResponse create(SchoolRequest  schoolRequest) {

        // find the all school
        if(schoolRepository.existsBySchoolName(schoolRequest.schoolName())){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"school name already exists");
        }

        School school = schoolMapper.formSchoolCreateRequest(schoolRequest);
        // save school to database
        schoolRepository.save(school);

     return schoolMapper.toSchoolResponse(school);

    }

    @Override
    public Page<SchoolResponse> getSchoolList(int page, int size) {

        // size  and page and size of the school
        if (page < 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"page cannot be less than 0");

        }

        if(size < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"size cannot be less than 1");
        }
        //
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<School> schools = schoolRepository.findAll(pageRequest);

        return schools.map(schoolMapper::toSchoolResponse);
    }

    @Override
    public SchoolResponse findSchoolByName(String name) {

        //TODO: find by name
        School school = schoolRepository.findBySchoolName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"school not found"));

        return schoolMapper.toSchoolResponse(school);
    }

    @Override
    public SchoolResponse findSchoolByID(Long id) {

        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"school not found"));

        return schoolMapper.toSchoolResponse(school);
    }

    @Override
    public void deleteSchoolById(Long id) {
        schoolRepository.deleteById(id);
    }

    @Override
    public SchoolResponse updateSchoolById(Long id, SchoolRequest schoolRequest) {
          // find the school that want update first
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"school not found"));

        schoolMapper.updateSchoolFormRequest(schoolRequest, school);
        schoolRepository.save(school);

        return schoolMapper.toSchoolResponse(school);
    }
}
