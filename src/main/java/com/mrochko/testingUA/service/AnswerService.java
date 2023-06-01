package com.mrochko.testingUA.service;

import com.mrochko.testingUA.dto.AnswerDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface AnswerService {

    AnswerDTO createAnswer(Long questionId, AnswerDTO answerDTO);

    List<AnswerDTO> getAllAnswers(Long questionId);

    void deleteAnswer(Long answerId);

    void updateAnswer(Long answerId, AnswerDTO answerDTO);

}
