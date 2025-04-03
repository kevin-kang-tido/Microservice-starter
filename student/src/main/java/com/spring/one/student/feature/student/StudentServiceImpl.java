package com.spring.one.student.feature.student;

import com.spring.one.student.domain.Student;
import com.spring.one.student.feature.student.dto.StudentRequest;
import com.spring.one.student.feature.student.dto.StudentResponse;
import com.spring.one.student.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private  final StudentMapper studentMapper;

    @Override
    public StudentResponse createStudent(StudentRequest studentRequest) {
        // find all user
        if(studentRepository.existsByName(studentRequest.name())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Student with name " + studentRequest.name() + " already exists!"
            );
        }

        Student student =  studentMapper.formStudentCreateRequest(studentRequest);

        studentRepository.save(student);

        return studentMapper.toStudentResponse(student);
    }

    @Override
    public Page<StudentResponse> getAllStudentList(int page, int size) {

        //  validate page and size
        if(page <0 ){
            throw  new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number cannot be less than zero!"
            );
        }

        if (size < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Size cannot be less than ONE!"
            );
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Student> students = studentRepository.findAll(pageRequest);

        return students.map(studentMapper::toStudentResponse);
    }

    @Override
    public StudentResponse getStudentByName(String name) {

        if (!studentRepository.existsByName(name)){
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student with name " + name + " does not exist!"
            );
        }

        Student student = studentRepository.findStudentByName(name)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student"+getStudentByName(name)+" not found!"
                ));

        return studentMapper.toStudentResponse(student);
    }

    @Override
    public StudentResponse getStudentById(Long id) {

        if (!studentRepository.existsById(id)){
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student does not exist!"
            );
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student"+getStudentById(id)+" not found!"
                ));

        return studentMapper.toStudentResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long id,StudentRequest studentRequest) {

        // find student first
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student"+getStudentById(id)+" not found!"
                ));

       studentMapper.updateStudentRequest(studentRequest, student);
       studentRepository.save(student);

        return studentMapper.toStudentResponse(student);
    }

    @Override
    public void deleteStudent(Long id) {

        // find student first
        if(!studentRepository.existsById(id)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student has not been found!"
            );

        }

        // save student
        studentRepository.deleteById(id);

    }
}
