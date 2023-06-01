package com.mrochko.testingUA.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.service.AnswerService;
import com.mrochko.testingUA.util.AnswerDataTestUtil;
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
@WebMvcTest(AnswerController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AnswerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnswerService answerService;

    @Test
    void testCreateAnswerForQuestion() throws Exception {
        AnswerDTO answerDTO = AnswerDataTestUtil.createAnswerDTO();
        when(answerService.createAnswer(anyLong(), any(AnswerDTO.class))).thenReturn(answerDTO);

        String jsonRequestBody = new ObjectMapper().writeValueAsString(answerDTO);

        mockMvc.perform(post("/api/v1/test/question/{questionId}/answer", 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(answerDTO.getId()))
                .andExpect(jsonPath("$.answerText").value(answerDTO.getAnswerText()))
                .andExpect(jsonPath("$.answerStatus").value(answerDTO.getAnswerStatus().name()));
    }

    @Test
    void testGetAllAnswersForQuestion() throws Exception {
        List<AnswerDTO> answers = AnswerDataTestUtil.createAnswerDtoList();
        when(answerService.getAllAnswers(anyLong())).thenReturn(answers);

        ResultActions result = mockMvc
                .perform(get("/api/v1/test/question/{questionId}/answer", 456L))
                .andDo(print())
                .andExpect(status().isOk());

        for (int i = 0; i < answers.size(); i++) {
            AnswerDTO answer = answers.get(i);
            result.andExpect(jsonPath("$[" + i + "].id").value(answer.getId()))
                    .andExpect(jsonPath("$[" + i + "].answerText").value(answer.getAnswerText()))
                    .andExpect(jsonPath("$[" + i + "].answerStatus").value(answer.getAnswerStatus().name()));
        }
    }

    @Test
    void testDeleteAnswer() throws Exception {
        Long answerId = 789L;
        doNothing().when(answerService).deleteAnswer(anyLong());

        mockMvc.perform(delete("/api/v1/test/question/answer/{answerId}", answerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(answerId.intValue()));
    }

    @Test
    void testUpdateAnswer() throws Exception {
        AnswerDTO answerDTO = AnswerDataTestUtil.createAnswerDTO();
        doNothing().when(answerService).updateAnswer(anyLong(), any(AnswerDTO.class));

        String jsonRequestBody = new ObjectMapper().writeValueAsString(answerDTO);

        mockMvc.perform(put("/api/v1/test/question/answer/{answerId}", answerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(answerDTO.getId().intValue()));
    }

}