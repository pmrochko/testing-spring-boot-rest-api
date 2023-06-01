package com.mrochko.testingUA.repository;

import com.mrochko.testingUA.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

        List<Question> findAllByTest_Id(Long testId);

        @Transactional
        @Modifying
        @Query("update Question q set q.questionText = ?1 where q.id = ?2")
        void updateQuestionTextById(String questionText, Long id);

}
