package com.mrochko.testingUA.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.service.QuestionService;
import com.mrochko.testingUA.util.QuestionDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(QuestionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QuestionService questionService;

    @Test
    void testCreateQuestionForTest() throws Exception {
        QuestionDTO questionDTO = QuestionDataTestUtil.createQuestionDTO();
        when(questionService.createQuestion(anyLong(), any(QuestionDTO.class))).thenReturn(questionDTO);

        String jsonRequestBody = new ObjectMapper().writeValueAsString(questionDTO);

        mockMvc.perform(post("/api/v1/test/{testId}/question", 222L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(questionDTO.getId()))
                .andExpect(jsonPath("$.questionText").value(questionDTO.getQuestionText()))
                .andExpect(jsonPath("$.answerList", hasSize(questionDTO.getAnswerList().size())));
    }

    @Test
    void testGetAllQuestionsForTest() throws Exception {
        List<QuestionDTO> questions = QuestionDataTestUtil.createQuestionDtoList();
        when(questionService.getAllQuestions(anyLong())).thenReturn(questions);

        ResultActions result = mockMvc.perform(get("/api/v1/test/{testId}/question", 987L))
                .andDo(print())
                .andExpect(status().isOk());

        for (int i = 0; i < questions.size(); i++) {
            QuestionDTO question = questions.get(i);
            result.andExpect(jsonPath("$[" + i + "].id").value(question.getId()))
                  .andExpect(jsonPath("$[" + i + "].questionText").value(question.getQuestionText()))
                  .andExpect(jsonPath("$[" + i + "].answerList", hasSize(question.getAnswerList().size())));
        }
    }

    @Test
    void testDeleteQuestion() throws Exception {
        Long questionId = 333L;
        doNothing().when(questionService).deleteQuestion(anyLong());

        mockMvc.perform(delete("/api/v1/test/question/{questionId}", questionId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(questionId.intValue()));
    }

    @Test
    void testUpdateQuestion() throws Exception {
        QuestionDTO questionDTO = QuestionDataTestUtil.createQuestionDTO();
        doNothing().when(questionService).updateQuestion(anyLong(), any(QuestionDTO.class));

        String jsonRequestBody = new ObjectMapper().writeValueAsString(questionDTO);

        mockMvc.perform(put("/api/v1/test/question/{questionId}", questionDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(questionDTO.getId().intValue()));
    }
}