package com.spring.one.microstarter.feature.school;

import com.spring.one.microstarter.domain.School;
import com.spring.one.microstarter.feature.school.dto.SchoolRequest;
import com.spring.one.microstarter.feature.school.dto.SchoolResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/school")
public class SchoolController {

    private final SchoolService schoolService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    SchoolResponse createSchool(@RequestBody SchoolRequest schoolRequest){
        return schoolService.create(schoolRequest);
    }
    // get by list


    @GetMapping
    Page<SchoolResponse> getSchoolList(
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "1") int size

    ){
        return schoolService.getSchoolList(page,size);

    }

    // get by name
    @GetMapping("/name/{name}")
    ResponseEntity<SchoolResponse> findSchoolByName(@PathVariable String name){

         SchoolResponse schoolResponse = schoolService.findSchoolByName(name);

        return ResponseEntity.ok(schoolResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<SchoolResponse> findSchoolById(@PathVariable Long id){
        SchoolResponse schoolResponse = schoolService.findSchoolByID(id);
        return ResponseEntity.ok(schoolResponse);
    }

    @DeleteMapping("/{id}")
    void deleteSchoolById(@PathVariable  Long id){
        schoolService.deleteSchoolById(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<SchoolResponse> updateSchoolById(@PathVariable Long id, @RequestBody SchoolRequest schoolRequest){

        schoolService.updateSchoolById(id, schoolRequest);

        return ResponseEntity.ok(schoolService.findSchoolByID(id));
    }
}
