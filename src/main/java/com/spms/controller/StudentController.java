package com.spms.controller;

import com.spms.model.Student;
import com.spms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Create a new student
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }
    @PostMapping("/bulk")
    public List<Student> createStudents(@RequestBody List<Student> students) {
        return studentRepository.saveAll(students);
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get one student by id
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    // Update a student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        student.setName(updatedStudent.getName());
        student.setRollNumber(updatedStudent.getRollNumber());
        student.setClassName(updatedStudent.getClassName());
        return studentRepository.save(student);
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "Student deleted with id: " + id;
    }
}