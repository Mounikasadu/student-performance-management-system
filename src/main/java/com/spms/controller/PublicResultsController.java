package com.spms.controller;

import com.spms.model.Grade;
import com.spms.model.Student;
import com.spms.repository.GradeRepository;
import com.spms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicResultsController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping("/results/{rollNumber}")
    public List<Grade> getResultsByRollNumber(@PathVariable String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new RuntimeException("No student found with roll number: " + rollNumber));
        return gradeRepository.findByStudent(student);
    }
}
