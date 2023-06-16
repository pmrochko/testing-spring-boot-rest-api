package com.mrochko.testingUA.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrochko.testingUA.dto.AnswersToQuestionDTO;
import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.mapper.HistoryOfTestMapper;
import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.service.TestService;
import com.mrochko.testingUA.util.HistoryOfTestDataTestUtil;
import com.mrochko.testingUA.util.TestDataTestUtil;
import com.mrochko.testingUA.util.UserDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
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
@WebMvcTest(TestController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class TestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TestService testService;

    public static final String TEST_API_URL = "/api/v1/tests";

    @Test
    void testCreateTest() throws Exception {
        TestDTO testEntity = TestDataTestUtil.createTestDTO();
        when(testService.createTest(any(TestDTO.class))).thenReturn(testEntity);

        String jsonRequestBody = new ObjectMapper().writeValueAsString(testEntity);

        mockMvc.perform(post(TEST_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testEntity.getId()))
                .andExpect(jsonPath("$.subjectId").value(testEntity.getSubjectId()))
                .andExpect(jsonPath("$.title").value(testEntity.getTitle()))
                .andExpect(jsonPath("$.description").value(testEntity.getDescription()))
                .andExpect(jsonPath("$.testDifficulty").value(testEntity.getTestDifficulty().name()))
                .andExpect(jsonPath("$.minutes").value(testEntity.getMinutes()))
                .andExpect(jsonPath("$.questionList", hasSize(testEntity.getQuestionList().size())));
    }

    @Test
    void testGetAllTests() throws Exception {
        List<TestDTO> tests = TestDataTestUtil.createTestDtoList();
        when(testService.getAllTests(null, null)).thenReturn(tests);

        ResultActions result = mockMvc.perform(get(TEST_API_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tests.size())));

        for (int i = 0; i < tests.size(); i++) {
            TestDTO test = tests.get(i);
            result.andExpect(jsonPath("$[" + i + "].id").value(test.getId()))
                    .andExpect(jsonPath("$[" + i + "].subjectId").value(test.getSubjectId()))
                    .andExpect(jsonPath("$[" + i + "].title").value(test.getTitle()))
                    .andExpect(jsonPath("$[" + i + "].description").value(test.getDescription()))
                    .andExpect(jsonPath("$[" + i + "].testDifficulty").value(test.getTestDifficulty().name()))
                    .andExpect(jsonPath("$[" + i + "].minutes").value(test.getMinutes()))
                    .andExpect(jsonPath("$[" + i + "].questionList", hasSize(test.getQuestionList().size())));
        }
    }

    @Test
    void testDeleteTest() throws Exception {
        Long testId = 133L;
        doNothing().when(testService).deleteTest(anyLong());

        mockMvc.perform(delete(TEST_API_URL + "/{testId}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(testId.intValue()));
    }

    @Test
    void testUpdateTest() throws Exception {
        TestDTO testDTO = TestDataTestUtil.createTestDTO();
        testDTO.setId(123L);
        doNothing().when(testService).updateTest(anyLong(), any(TestDTO.class));

        String jsonRequestBody = new ObjectMapper().writeValueAsString(testDTO);

        mockMvc.perform(put(TEST_API_URL + "/{testId}", testDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(testDTO.getId().intValue()));
    }

    @Test
    @WithMockUser
    void testStartTest() throws Exception {
        TestDTO testDTO = TestDataTestUtil.createTestDTO();
        when(testService.startTest(any(UserDetails.class), anyLong())).thenReturn(testDTO);

        mockMvc.perform(post(TEST_API_URL + "/{testId}/start", 456L))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testDTO.getId()))
                .andExpect(jsonPath("$.subjectId").value(testDTO.getSubjectId()))
                .andExpect(jsonPath("$.title").value(testDTO.getTitle()))
                .andExpect(jsonPath("$.description").value(testDTO.getDescription()))
                .andExpect(jsonPath("$.testDifficulty").value(testDTO.getTestDifficulty().name()))
                .andExpect(jsonPath("$.minutes").value(testDTO.getMinutes()))
                .andExpect(jsonPath("$.questionList", hasSize(testDTO.getQuestionList().size())));
    }

    @Test
    @WithMockUser
    void testFinishTest() throws Exception {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        User userEntity = UserDataTestUtil.createUser();
        HistoryOfTest historyOfTest = HistoryOfTestDataTestUtil
                .createFinishHistoryOfTestRecord(userEntity, testEntity);
        HistoryOfTestDTO historyOfTestDTO = HistoryOfTestMapper.INSTANCE
                .mapToHistoryOfTestDTO(historyOfTest);
        List<AnswersToQuestionDTO> requestAnswers = TestDataTestUtil.createAnswersToQuestionList();

        String jsonRequestBody = new ObjectMapper().writeValueAsString(requestAnswers);
        when(testService.finishTest(any(UserDetails.class), anyLong(), any())).thenReturn(historyOfTestDTO);

        mockMvc.perform(put(TEST_API_URL + "/{testId}/submit", 998L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(historyOfTestDTO.getId()))
                .andExpect(jsonPath("$.studentName")
                        .value(historyOfTestDTO.getStudentName()))
                .andExpect(jsonPath("$.testTitle")
                        .value(historyOfTestDTO.getTestTitle()))
                .andExpect(jsonPath("$.testProgressStatus")
                        .value(historyOfTestDTO.getTestProgressStatus().name()))
                .andExpect(jsonPath("$.resultInPercent")
                        .value(historyOfTestDTO.getResultInPercent()));
    }

}