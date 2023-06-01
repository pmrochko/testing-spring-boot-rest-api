package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.model.Answer;
import com.mrochko.testingUA.model.Question;
import com.mrochko.testingUA.repository.AnswerRepository;
import com.mrochko.testingUA.repository.QuestionRepository;
import com.mrochko.testingUA.util.AnswerDataTestUtil;
import com.mrochko.testingUA.util.QuestionDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @InjectMocks
    AnswerServiceImpl answerService;

    @Mock
    AnswerRepository answerRepository;
    @Mock
    QuestionRepository questionRepository;

    @Test
    void createAnswer() {
        Answer answer = AnswerDataTestUtil.createAnswer();
        AnswerDTO answerDTO = AnswerDataTestUtil.createAnswerDTO();
        Question question = QuestionDataTestUtil.createQuestion();
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
        when(answerRepository.save(any())).thenReturn(answer);

        AnswerDTO resultAnswerDTO = answerService.createAnswer(111L, answerDTO);

        assertEqualsAnswerProperties(resultAnswerDTO, answer);
    }

    @Test
    void createAnswer_QuestionNotFound() {
        AnswerDTO answerDTO = AnswerDataTestUtil.createAnswerDTO();
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> answerService.createAnswer(333L, answerDTO));
    }

    @Test
    void getAllAnswers() {
        List<Answer> answerList = AnswerDataTestUtil.createAnswerList();
        when(questionRepository.existsById(anyLong())).thenReturn(true);
        when(answerRepository.findAllByQuestion_Id(anyLong())).thenReturn(answerList);

        List<AnswerDTO> resultAnswerDtoList = answerService.getAllAnswers(877L);

        for (int i = 0; i < answerList.size(); i++) {
            Answer answer = answerList.get(i);
            AnswerDTO answerDTO = resultAnswerDtoList.get(i);

            assertEqualsAnswerProperties(answerDTO, answer);
        }
    }

    @Test
    void getAllAnswers_QuestionNotFound() {
        when(questionRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class,
                () -> answerService.getAllAnswers(992L));
    }

    @Test
    void deleteAnswer() {
        Answer answer = AnswerDataTestUtil.createAnswer();
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
        doNothing().when(answerRepository).delete(answer);

        answerService.deleteAnswer(98712L);

        verify(answerRepository, times(1)).delete(answer);
    }

    @Test
    void deleteAnswer_AnswerNotFound() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> answerService.deleteAnswer(76L));
    }

    @Test
    void updateAnswer() {
        Answer answer = AnswerDataTestUtil.createAnswer();
        AnswerDTO answerDTO = AnswerDataTestUtil.createAnswerDTO();
        answer.setId(answerDTO.getId());

        when(answerRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(answerRepository).updateAnswer(any(), anyString(), anyLong());

        answerService.updateAnswer(answerDTO.getId(), answerDTO);

        verify(answerRepository, times(1))
                .updateAnswer(answer.getAnswerStatus(), answer.getAnswerText(), answer.getId());
    }

    @Test
    void updateAnswer_AnswerNotFound() {
        AnswerDTO answerDTO = AnswerDataTestUtil.createAnswerDTO();
        when(answerRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class,
                () -> answerService.updateAnswer(93L, answerDTO));
    }

    private void assertEqualsAnswerProperties(AnswerDTO answerDTO, Answer answer) {
        assertThat(answerDTO, allOf(
                hasProperty("id", equalTo(answer.getId())),
                hasProperty("answerStatus", equalTo(answer.getAnswerStatus())),
                hasProperty("answerText", equalTo(answer.getAnswerText()))
        ));
    }

}