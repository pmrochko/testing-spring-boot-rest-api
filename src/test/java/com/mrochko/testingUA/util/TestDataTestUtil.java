package com.mrochko.testingUA.util;

import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.dto.AnswersToQuestionDTO;
import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.mapper.TestMapper;
import com.mrochko.testingUA.model.Test;
import com.mrochko.testingUA.model.enums.TestDifficulty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pavlo Mrochko
 */
public class TestDataTestUtil {

    public static final Long TEST_ID = 1541L;
    public static final Long TEST_SUBJECT_ID = 246L;
    public static final String TEST_SUBJECT_NAME = "Subject Name";
    public static final String TEST_TITLE = "Test title";
    public static final String TEST_DESC = "Test desc";
    public static final TestDifficulty TEST_DIFF = TestDifficulty.DIFFICULT;
    public static final Integer TEST_MINUTES = 25;
    public static final int TEST_LIST_SIZE = 6;
    public static final int ANSWERS_TO_QUESTION_LIST_SIZE = 5;

    public static TestDTO createTestDTO() {
        return TestDTO.builder()
                .id(TEST_ID)
                .subjectId(TEST_SUBJECT_ID)
                .title(TEST_TITLE)
                .description(TEST_DESC)
                .testDifficulty(TEST_DIFF)
                .minutes(TEST_MINUTES)
                .questionList(QuestionDataTestUtil.createQuestionDtoList())
                .build();
    }

    public static Test createTest() {
        Test test = TestMapper.INSTANCE.mapToTest(createTestDTO());
        test.getSubject().setName(TEST_SUBJECT_NAME);

        return test;
    }

    public static List<TestDTO> createTestDtoList() {
        List<TestDTO> testDtoList = new ArrayList<>();
        for (int i = 0; i < TEST_LIST_SIZE; i++) {
            testDtoList.add(
                    TestDTO.builder()
                            .id(TEST_ID + i)
                            .subjectId(TEST_SUBJECT_ID + i)
                            .title(TEST_TITLE + "_" + i)
                            .description(TEST_DESC + "_" + i)
                            .testDifficulty(TEST_DIFF)
                            .minutes(TEST_MINUTES + i)
                            .questionList(QuestionDataTestUtil.createQuestionDtoList())
                            .build()
            );
        }
        return testDtoList;
    }

    public static List<Test> createTestList() {
        List<Test> testList = TestMapper.INSTANCE.mapToListOfTests(createTestDtoList());
        for (Test test : testList) {
            test.getSubject().setName(TEST_SUBJECT_NAME + "_" + test.getSubject().getId());
        }

        return testList;
    }

    public static List<AnswersToQuestionDTO> createAnswersToQuestionList() {
        List<AnswersToQuestionDTO> list = new ArrayList<>();
        Long questionId = QuestionDataTestUtil.createQuestionDTO().getId();
        Set<Long> answersId = AnswerDataTestUtil.createAnswerDtoList().stream()
                .map(AnswerDTO::getId).collect(Collectors.toSet());
        for (int i = 0; i < ANSWERS_TO_QUESTION_LIST_SIZE; i++) {
            questionId += i;
            list.add(
                    AnswersToQuestionDTO.builder()
                            .questionId(questionId)
                            .selectedAnswersIds(answersId)
                            .build()
            );
        }
        return list;
    }

}
