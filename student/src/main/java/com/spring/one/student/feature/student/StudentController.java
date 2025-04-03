package com.spring.one.student.feature.student;

import com.spring.one.student.domain.Student;
import com.spring.one.student.feature.student.dto.StudentRequest;
import com.spring.one.student.feature.student.dto.StudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/create")
    StudentResponse create(@RequestBody StudentRequest studentRequest) {
        return  studentService.createStudent(studentRequest);

    }

    @GetMapping
    Page<StudentResponse> getAllStudentsList(
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "33") int size

    ){
        return studentService.getAllStudentList(page, size);

    }

    // TODO: find by name
    @GetMapping("/name/{name}")
    StudentResponse getStudentByName(@PathVariable String name) {
        return studentService.getStudentByName(name);
    }

    // find by id
    @GetMapping("/{id}")
    StudentResponse findStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // TODO: update student
    @PutMapping("/{id}")
    StudentResponse updateStudent(@PathVariable Long id, @RequestBody StudentRequest studentRequest) {
        return studentService.updateStudent(id, studentRequest);
    }

    @DeleteMapping()
    void deleteStudentById(@RequestParam Long id) {
        studentService.deleteStudent(id);
    }



}
