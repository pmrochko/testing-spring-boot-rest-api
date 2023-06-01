package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.model.Question;
import com.mrochko.testingUA.repository.QuestionRepository;
import com.mrochko.testingUA.repository.TestRepository;
import com.mrochko.testingUA.util.QuestionDataTestUtil;
import com.mrochko.testingUA.util.TestDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @InjectMocks
    QuestionServiceImpl questionService;

    @Mock
    QuestionRepository questionRepository;
    @Mock
    TestRepository testRepository;

    @Test
    void createQuestion() {
        Question testQuestion = QuestionDataTestUtil.createQuestion();
        QuestionDTO testQuestionDto = QuestionDataTestUtil.createQuestionDTO();
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(432L);

        when(testRepository.findById(anyLong())).thenReturn(Optional.of(testEntity));
        when(questionRepository.save(any())).thenReturn(testQuestion);

        QuestionDTO resultQuestionDto = questionService.createQuestion(testEntity.getId(), testQuestionDto);

        assertEqualsQuestionProperties(resultQuestionDto, testQuestion);
    }

    @Test
    void createQuestion_TestNotFound() {
        QuestionDTO questionDTO = QuestionDataTestUtil.createQuestionDTO();
        when(testRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> questionService.createQuestion(345L, questionDTO));
    }

    @Test
    void getAllQuestions() {
        List<Question> questionList = QuestionDataTestUtil.createQuestionList();

        when(testRepository.existsById(anyLong())).thenReturn(true);
        when(questionRepository.findAllByTest_Id(anyLong())).thenReturn(questionList);

        List<QuestionDTO> questionDtoList = questionService.getAllQuestions(712L);

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            QuestionDTO questionDTO = questionDtoList.get(i);

            assertEqualsQuestionProperties(questionDTO, question);
        }
    }

    @Test
    void getAllQuestions_TestNotFound() {
        when(testRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class,
                () -> questionService.getAllQuestions(172L));
    }

    @Test
    void deleteQuestion() {
        Question question = QuestionDataTestUtil.createQuestion();
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
        doNothing().when(questionRepository).delete(question);

        questionService.deleteQuestion(123L);

        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void deleteQuestion_QuestionNotFound() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> questionService.deleteQuestion(897L));
    }

    @Test
    void updateQuestion() {
        QuestionDTO questionDTO = QuestionDataTestUtil.createQuestionDTO();
        when(questionRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(questionRepository).updateQuestionTextById(anyString(), anyLong());

        questionService.updateQuestion(1724L, questionDTO);

        verify(questionRepository, times(1))
                .updateQuestionTextById(questionDTO.getQuestionText(), 1724L);
    }

    @Test
    void updateQuestion_QuestionNotFound() {
        QuestionDTO questionDTO = QuestionDataTestUtil.createQuestionDTO();
        when(questionRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> questionService.updateQuestion(222L, questionDTO));
    }

    private void assertEqualsQuestionProperties(QuestionDTO questionDTO, Question question) {
        assertThat(questionDTO, allOf(
                hasProperty("id", equalTo(question.getId())),
                hasProperty("questionText", equalTo(question.getQuestionText())),
                hasProperty("answerList", hasSize(question.getAnswerList().size()))
        ));
    }

}
