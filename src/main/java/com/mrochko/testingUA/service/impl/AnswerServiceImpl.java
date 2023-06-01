package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.mapper.AnswerMapper;
import com.mrochko.testingUA.model.Answer;
import com.mrochko.testingUA.model.Question;
import com.mrochko.testingUA.repository.AnswerRepository;
import com.mrochko.testingUA.repository.QuestionRepository;
import com.mrochko.testingUA.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public AnswerDTO createAnswer(Long questionId, AnswerDTO answerDTO) {
        Answer answer = AnswerMapper.INSTANCE.mapToAnswer(answerDTO);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(EntityNotFoundException::new);
        answer.setQuestion(question);
        answer = answerRepository.save(answer);
        log.info("New answer was created successfully");
        return AnswerMapper.INSTANCE.mapToAnswerDTO(answer);
    }

    @Override
    public List<AnswerDTO> getAllAnswers(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new EntityNotFoundException("Question was not found");
        }
        List<Answer> answerList = answerRepository.findAllByQuestion_Id(questionId);
        log.info("Successful getting a list of answers from the repository");
        return AnswerMapper.INSTANCE.mapToListOfAnswersDTO(answerList);
    }

    @Override
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(EntityNotFoundException::new);
        answerRepository.delete(answer);
        log.info("The answer was deleted successfully");
    }

    @Override
    public void updateAnswer(Long answerId, AnswerDTO answerDTO) {
        if (!answerRepository.existsById(answerId)) {
            throw new EntityNotFoundException("Answer was not found");
        }
        answerRepository.updateAnswer(
                answerDTO.getAnswerStatus(),
                answerDTO.getAnswerText(),
                answerId
        );
        log.info("The answer was updated successfully");
    }

}
