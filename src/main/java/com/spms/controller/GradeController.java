package com.spms.controller;

import com.spms.model.Grade;
import com.spms.model.Student;
import com.spms.model.Subject;
import com.spms.repository.GradeRepository;
import com.spms.repository.StudentRepository;
import com.spms.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Create a grade - expects studentId and subjectId in request body
    @PostMapping
    public Grade createGrade(@RequestBody GradeRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + request.getSubjectId()));

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setMarks(request.getMarks());
        grade.setExamType(request.getExamType());
        grade.setExamDate(request.getExamDate());

        return gradeRepository.save(grade);
    }
    
    @PostMapping("/bulk")
    public List<Grade> createGrades(@RequestBody List<GradeRequest> requests) {
        List<Grade> grades = new java.util.ArrayList<>();
        for (GradeRequest request : requests) {
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));
            Subject subject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + request.getSubjectId()));

            Grade grade = new Grade();
            grade.setStudent(student);
            grade.setSubject(subject);
            grade.setMarks(request.getMarks());
            grade.setExamType(request.getExamType());
            grade.setExamDate(request.getExamDate());
            grades.add(grade);
        }
        return gradeRepository.saveAll(grades);
    }

    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Grade getGradeById(@PathVariable Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public String deleteGrade(@PathVariable Long id) {
        gradeRepository.deleteById(id);
        return "Grade deleted with id: " + id;
    }
}
