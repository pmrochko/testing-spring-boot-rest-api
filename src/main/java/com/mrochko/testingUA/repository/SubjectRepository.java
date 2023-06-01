package com.mrochko.testingUA.repository;

import com.mrochko.testingUA.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Pavlo Mrochko
 */
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}