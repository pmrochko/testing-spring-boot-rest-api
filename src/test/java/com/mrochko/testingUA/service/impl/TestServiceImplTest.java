package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.AnswersToQuestionDTO;
import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.model.Subject;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.TestDifficulty;
import com.mrochko.testingUA.model.enums.TestProgressStatus;
import com.mrochko.testingUA.model.enums.TestSorting;
import com.mrochko.testingUA.repository.*;
import com.mrochko.testingUA.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
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
class TestServiceImplTest {

    @InjectMocks
    TestServiceImpl testService;

    @Mock
    TestRepository testRepository;
    @Mock
    SubjectRepository subjectRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    QuestionRepository questionRepository;
    @Mock
    AnswerRepository answerRepository;
    @Mock
    HistoryOfTestRepository historyOfTestRepository;

    @Test
    void createTest() {
        Subject testSubject = SubjectDataTestUtil.createSubject();
        com.mrochko.testingUA.model.Test testTest = TestDataTestUtil.createTest();
        TestDTO testTestDTO = TestDataTestUtil.createTestDTO();
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(testSubject));
        when(testRepository.save(any())).thenReturn(testTest);

        TestDTO testDTO = testService.createTest(testTestDTO);

        assertEqualsTestProperties(testDTO, testTest);
    }

    @Test
    void createTest_SubjectNotFound() {
        TestDTO testTestDTO = TestDataTestUtil.createTestDTO();
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> testService.createTest(testTestDTO));
    }

    @Test
    void getAllTests() {
        List<com.mrochko.testingUA.model.Test> testTestList = TestDataTestUtil.createTestList();
        when(testRepository.findAll()).thenReturn(testTestList);

        List<TestDTO> testDtoList = testService.getAllTests(null, null);

        for (int i = 0; i < testTestList.size(); i++) {
            com.mrochko.testingUA.model.Test test = testTestList.get(i);
            TestDTO testDTO = testDtoList.get(i);

            assertEqualsTestProperties(testDTO, test);
        }
    }

    @Test
    void getAllTests_WithSortingByTitleDesc() {
        List<com.mrochko.testingUA.model.Test> testList = TestDataTestUtil.createTestList();
        when(testRepository.findAll(Sort.by(Sort.Direction.DESC, "title"))).thenReturn(testList);

        List<TestDTO> testDtoList = testService.getAllTests(TestSorting.BY_TITLE_DOWN.name(), null);

        for (int i = 0; i < testList.size(); i++) {
            com.mrochko.testingUA.model.Test test = testList.get(i);
            TestDTO testDTO = testDtoList.get(i);

            assertEqualsTestProperties(testDTO, test);
        }
    }

    @Test
    void getAllTests_WithFilterBySubject() {
        List<com.mrochko.testingUA.model.Test> testList = TestDataTestUtil.createTestList();
        Subject subject = testList.get(0).getSubject();
        when(testRepository.findAllBySubject_Name(anyString())).thenReturn(testList);

        List<TestDTO> testDtoList = testService.getAllTests(null, subject.getName());

        for (int i = 0; i < testList.size(); i++) {
            com.mrochko.testingUA.model.Test test = testList.get(i);
            TestDTO testDTO = testDtoList.get(i);

            assertEqualsTestProperties(testDTO, test);
        }
    }

    @Test
    void getAllTests_WithFilterAndSorting() {
        List<com.mrochko.testingUA.model.Test> testList = TestDataTestUtil.createTestList();
        Subject filterSubject = testList.get(testList.size() % 2).getSubject();
        TestSorting sorting = TestSorting.BY_DIFFICULTY_DOWN;
        when(testRepository.findAllBySubject_NameOrderByTestDifficultyDesc(filterSubject.getName()))
                .thenReturn(testList);

        List<TestDTO> testDtoList = testService.getAllTests(sorting.name(), filterSubject.getName());

        for (int i = 0; i < testList.size(); i++) {
            com.mrochko.testingUA.model.Test test = testList.get(i);
            TestDTO testDTO = testDtoList.get(i);

            assertEqualsTestProperties(testDTO, test);
        }
    }

    @Test
    void deleteTest() {
        com.mrochko.testingUA.model.Test testTest = TestDataTestUtil.createTest();
        testTest.setId(888L);
        when(testRepository.findById(anyLong())).thenReturn(Optional.of(testTest));
        doNothing().when(testRepository).delete(testTest);

        testService.deleteTest(testTest.getId());

        verify(testRepository, times(1)).delete(testTest);
    }

    @Test
    void deleteTest_TestNotFound() {
        when(testRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> testService.deleteTest(990L));
    }

    @Test
    void updateTest() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        TestDTO testDTO = TestDataTestUtil.createTestDTO();

        when(testRepository.existsById(anyLong())).thenReturn(true);
        when(subjectRepository.findById(anyLong())).thenReturn(Optional.of(testEntity.getSubject()));
        doNothing().when(testRepository).updateTest(
                any(Subject.class), anyString(), anyString(),
                any(TestDifficulty.class), anyInt(), anyLong()
        );

        testService.updateTest(123L, testDTO);

        verify(testRepository, times(1))
                .updateTest(testEntity.getSubject(), testEntity.getTitle(), testEntity.getDescription(),
                        testEntity.getTestDifficulty(), testEntity.getMinutes(), 123L);
    }

    @Test
    void startTest() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(111L);
        User userEntity = UserDataTestUtil.createUser();
        userEntity.setId(222L);
        HistoryOfTest historyOfTestRecord = HistoryOfTestDataTestUtil
                .createStartHistoryOfTestRecord(userEntity, testEntity);

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(userEntity));
        when(testRepository.findById(anyLong())).thenReturn(Optional.of(testEntity));
        when(historyOfTestRepository.save(any())).thenReturn(historyOfTestRecord);

        TestDTO testDTO = testService.startTest(
                UserDataTestUtil.createUserDetails(),
                testEntity.getId()
        );

        verify(historyOfTestRepository, times(1)).save(any());
        assertEqualsTestProperties(testDTO, testEntity);
    }

    @Test
    void startTest_UserNotFound() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(111L);
        User userEntity = UserDataTestUtil.createUser();
        userEntity.setId(222L);

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> testService.startTest(
                        UserDataTestUtil.createUserDetails(),
                        testEntity.getId()
                ));
    }

    @Test
    void startTest_TestNotFound() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(111L);
        User userEntity = UserDataTestUtil.createUser();
        userEntity.setId(222L);

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(userEntity));
        when(testRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> testService.startTest(
                        UserDataTestUtil.createUserDetails(),
                        testEntity.getId()
                ));
    }

    @Test
    void finishTest() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(111L);
        User userEntity = UserDataTestUtil.createUser();
        userEntity.setId(222L);
        HistoryOfTest finishTestRecord = HistoryOfTestDataTestUtil
                .createFinishHistoryOfTestRecord(userEntity, testEntity);
        List<AnswersToQuestionDTO> selectedAnswersToQuestions = TestDataTestUtil.createAnswersToQuestionList();

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(userEntity));
        when(testRepository.findById(anyLong())).thenReturn(Optional.of(testEntity));
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(QuestionDataTestUtil.createQuestion()));
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(AnswerDataTestUtil.createAnswer()));
        when(historyOfTestRepository.existsByUser_IdAndTest_IdAndTestProgressStatus(
                anyLong(),
                anyLong(),
                any(TestProgressStatus.class))
        ).thenReturn(true);
        when(historyOfTestRepository.findByUser_IdAndCompletionTime(
                anyLong(),
                any(Timestamp.class)
        )).thenReturn(Optional.ofNullable(finishTestRecord));

        HistoryOfTestDTO result = testService
                .finishTest(
                        UserDataTestUtil.createUserDetails(),
                        testEntity.getId(),
                        selectedAnswersToQuestions
                );

        assertEqualsHistoryOfTestProperties(result, finishTestRecord);
    }

    @Test
    void finishTest_UserNotFound() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(111L);
        User userEntity = UserDataTestUtil.createUser();
        userEntity.setId(222L);
        List<AnswersToQuestionDTO> selectedAnswersToQuestions = TestDataTestUtil.createAnswersToQuestionList();

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> testService.finishTest(
                        UserDataTestUtil.createUserDetails(),
                        testEntity.getId(),
                        selectedAnswersToQuestions
                ));
    }

    @Test
    void finishTest_TestNotFound() {
        com.mrochko.testingUA.model.Test testEntity = TestDataTestUtil.createTest();
        testEntity.setId(111L);
        User userEntity = UserDataTestUtil.createUser();
        userEntity.setId(222L);
        List<AnswersToQuestionDTO> selectedAnswersToQuestions = TestDataTestUtil.createAnswersToQuestionList();

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(userEntity));
        when(testRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> testService.finishTest(
                        UserDataTestUtil.createUserDetails(),
                        testEntity.getId(),
                        selectedAnswersToQuestions
                ));
    }

    private void assertEqualsTestProperties(TestDTO testDTO, com.mrochko.testingUA.model.Test testTest) {
        assertThat(testDTO, allOf(
                hasProperty("title", equalTo(testTest.getTitle())),
                hasProperty("description", equalTo(testTest.getDescription())),
                hasProperty("testDifficulty", equalTo(testTest.getTestDifficulty())),
                hasProperty("minutes", equalTo(testTest.getMinutes())),
                hasProperty("subjectId", equalTo(testTest.getSubject().getId()))
        ));
    }

    private void assertEqualsHistoryOfTestProperties(HistoryOfTestDTO recordDTO, HistoryOfTest record) {
        assertThat(recordDTO, allOf(
                hasProperty("id", equalTo(record.getId())),
                hasProperty("testProgressStatus", equalTo(record.getTestProgressStatus())),
                hasProperty("resultInPercent", equalTo(record.getResultInPercent()))
        ));
    }

}
