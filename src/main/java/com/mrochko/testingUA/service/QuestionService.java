package com.mrochko.testingUA.service;

import com.mrochko.testingUA.dto.QuestionDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface QuestionService {

    QuestionDTO createQuestion(Long testId, QuestionDTO questionDTO);

    List<QuestionDTO> getAllQuestions(Long testId);

    void deleteQuestion(Long questionId);

    void updateQuestion(Long questionId, QuestionDTO questionDTO);

}
