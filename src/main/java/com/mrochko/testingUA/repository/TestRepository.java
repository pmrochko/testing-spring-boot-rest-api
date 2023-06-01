package com.mrochko.testingUA.repository;

import com.mrochko.testingUA.model.Subject;
import com.mrochko.testingUA.model.Test;
import com.mrochko.testingUA.model.enums.TestDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findAllBySubject_NameOrderByTitleAsc (String subjectName);
    List<Test> findAllBySubject_NameOrderByTitleDesc (String subjectName);
    List<Test> findAllBySubject_NameOrderByTestDifficultyAsc (String subjectName);
    List<Test> findAllBySubject_NameOrderByTestDifficultyDesc (String subjectName);
    List<Test> findAllBySubject_NameOrderByQuestionListAsc (String subjectName);
    List<Test> findAllBySubject_NameOrderByQuestionListDesc (String subjectName);
    List<Test> findAllBySubject_Name (String subjectName);

    @Transactional
    @Modifying
    @Query("update Test t set t.subject = ?1, " +
                              "t.title = ?2, " +
                              "t.description = ?3, " +
                              "t.testDifficulty = ?4, " +
                              "t.minutes = ?5 " +
            "where t.id=?6")
    void updateTest(Subject subject,
                    String title,
                    String description,
                    TestDifficulty testDifficulty,
                    Integer minutes,
                    Long id);

}