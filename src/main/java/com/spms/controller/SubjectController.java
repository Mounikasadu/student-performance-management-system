package com.spms.controller;

import com.spms.model.Subject;
import com.spms.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }
    
    @PostMapping("/bulk")
    public List<Subject> createSubjects(@RequestBody List<Subject> subjects) {
    	return subjectRepository.saveAll(subjects);
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    @PutMapping("/{id}")
    public Subject updateSubject(@PathVariable Long id, @RequestBody Subject updatedSubject) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        subject.setName(updatedSubject.getName());
        subject.setTeacher(updatedSubject.getTeacher());
        return subjectRepository.save(subject);
    }

    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Long id) {
        subjectRepository.deleteById(id);
        return "Subject deleted with id: " + id;
    }
}