package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.mapper.QuestionMapper;
import com.mrochko.testingUA.model.Question;
import com.mrochko.testingUA.model.Test;
import com.mrochko.testingUA.repository.QuestionRepository;
import com.mrochko.testingUA.repository.TestRepository;
import com.mrochko.testingUA.service.QuestionService;
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
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    @Override
    public QuestionDTO createQuestion(Long testId, QuestionDTO questionDTO) {
        Question question = QuestionMapper.INSTANCE.mapToQuestion(questionDTO);
        Test test = testRepository.findById(testId)
                .orElseThrow(EntityNotFoundException::new);
        question.setTest(test);
        question = questionRepository.save(question);
        log.info("New question was created successfully");
        return QuestionMapper.INSTANCE.mapToQuestionDTO(question);
    }

    @Override
    public List<QuestionDTO> getAllQuestions(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new EntityNotFoundException("Test was not found");
        }
        List<Question> questionList = questionRepository.findAllByTest_Id(testId);
        log.info("Successful getting a list of questions from the repository");
        return QuestionMapper.INSTANCE.mapToListOfQuestionsDTO(questionList);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(EntityNotFoundException::new);
        questionRepository.delete(question);
        log.info("The question was deleted successfully");
    }

    @Override
    public void updateQuestion(Long questionId, QuestionDTO questionDTO) {
        if (!questionRepository.existsById(questionId)) {
            throw new EntityNotFoundException("Question was not found");
        }
        questionRepository.updateQuestionTextById(questionDTO.getQuestionText(), questionId);
        log.info("The question was updated successfully");
    }
}
