package com.spms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spms.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
