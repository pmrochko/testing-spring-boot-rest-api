package com.mrochko.testingUA.service;

import com.mrochko.testingUA.dto.AnswersToQuestionDTO;
import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.TestDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface TestService {

    TestDTO createTest(TestDTO testDTO);

    List<TestDTO> getAllTests(String sorting, String subject);

    void deleteTest(long testId);

    void updateTest(long testId, TestDTO testDTO);

    TestDTO startTest(UserDetails userDetails, long testId);

    HistoryOfTestDTO finishTest(UserDetails userDetails, long testId, List<AnswersToQuestionDTO> selectedAnswers);

}
