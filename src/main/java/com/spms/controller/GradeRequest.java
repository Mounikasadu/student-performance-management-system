package com.spms.controller;

import java.time.LocalDate;

public class GradeRequest {
    private Long studentId;
    private Long subjectId;
    private Double marks;
    private String examType;
    private LocalDate examDate;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }
    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
}
