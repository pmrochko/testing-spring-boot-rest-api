package com.mrochko.testingUA.repository;

import com.mrochko.testingUA.model.Answer;
import com.mrochko.testingUA.model.enums.AnswerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByQuestion_Id(Long questionId);

    @Transactional
    @Modifying
    @Query("update Answer a set a.answerStatus = ?1, a.answerText = ?2 where a.id = ?3")
    void updateAnswer(AnswerStatus answerStatus, String answerText, Long id);

}
