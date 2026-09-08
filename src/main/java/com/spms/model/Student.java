package com.spms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String rollNumber;

    private String className;
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRollNumber() { return rollNumber; }
    public String getClassName() { return className; }

    // Manual setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public void setClassName(String className) { this.className = className; }
}