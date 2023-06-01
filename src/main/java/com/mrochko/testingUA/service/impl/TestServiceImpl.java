package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.AnswersToQuestionDTO;
import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.exception.ServiceException;
import com.mrochko.testingUA.exception.TestAlreadyStartedException;
import com.mrochko.testingUA.exception.TestHasNotStartedException;
import com.mrochko.testingUA.mapper.AnswerMapper;
import com.mrochko.testingUA.mapper.HistoryOfTestMapper;
import com.mrochko.testingUA.mapper.QuestionMapper;
import com.mrochko.testingUA.mapper.TestMapper;
import com.mrochko.testingUA.model.*;
import com.mrochko.testingUA.model.enums.AnswerStatus;
import com.mrochko.testingUA.model.enums.TestProgressStatus;
import com.mrochko.testingUA.model.enums.TestSorting;
import com.mrochko.testingUA.repository.*;
import com.mrochko.testingUA.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * @author Pavlo Mrochko
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    private final TestRepository testRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final HistoryOfTestRepository historyRepository;

    @Override
    public TestDTO createTest(TestDTO testDTO) {
        Subject subject = subjectRepository.findById(testDTO.getSubjectId())
                .orElseThrow(EntityNotFoundException::new);
        Test test = TestMapper.INSTANCE.mapToTest(testDTO);
        test.setSubject(subject);
        test = testRepository.save(test);

        log.info("New test was created successfully");
        return TestMapper.INSTANCE.mapToTestDTO(test);
    }

    @Override
    public List<TestDTO> getAllTests(String sorting, String subject) {

        List<Test> testList;

        if (subject != null && sorting != null){
            testList = filterBySubjectAndSort(subject, sorting);
        } else if (subject != null) {
            testList = testRepository.findAllBySubject_Name(subject);
        } else if (sorting != null) {
            testList = sortTestList(sorting);
        } else {
            testList = testRepository.findAll();
        }

        List<TestDTO> testDtoList = TestMapper.INSTANCE.mapToListOfTestsDTO(testList);
        // remove questions
        testDtoList.forEach(t -> t.setQuestionList(null));

        log.info("Successful getting a list of tests from the repository");
        return testDtoList;
    }

    @Override
    public void deleteTest(long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(EntityNotFoundException::new);
        testRepository.delete(test);
        log.info("The test was deleted successfully");
    }

    @Override
    public void updateTest(long testId, TestDTO testDTO) {
        if (!testRepository.existsById(testId)) {
            throw new EntityNotFoundException("Test was not found");
        }
        Subject subject = subjectRepository.findById(testDTO.getSubjectId())
                .orElseThrow(EntityNotFoundException::new);
        testRepository.updateTest(
                subject,
                testDTO.getTitle(),
                testDTO.getDescription(),
                testDTO.getTestDifficulty(),
                testDTO.getMinutes(),
                testId
        );
        log.info("The test was updated successfully");
    }

    @Override
    public TestDTO startTest(UserDetails userDetails, long testId) {
        User user = userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(EntityNotFoundException::new);

        if(someOfTestsHaveAlreadyStarted(user.getId()))
            throw new TestAlreadyStartedException();

        Test test = testRepository.findById(testId)
                .orElseThrow(EntityNotFoundException::new);

        HistoryOfTest record = HistoryOfTest.builder()
                .user(user)
                .test(test)
                .completionTime(new Timestamp(System.currentTimeMillis()))
                .resultInPercent(0)
                .testProgressStatus(TestProgressStatus.STARTED)
                .build();

        historyRepository.save(record);
        log.info("The test(id:{}) was started successfully", testId);

        TestDTO testDTO = TestMapper.INSTANCE.mapToTestDTO(test);
        // remove answer statuses in Test.questionList.answerList
        testDTO.getQuestionList()
                .stream()
                .flatMap(q -> q.getAnswerList().stream())
                .forEach(a -> a.setAnswerStatus(null));

        return testDTO;
    }

    @Override
    public HistoryOfTestDTO finishTest(UserDetails userDetails, long testId,
                                       List<AnswersToQuestionDTO> selectedAnswersToQuestions) {
        User user = userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(EntityNotFoundException::new);

        Test test = testRepository.findById(testId)
                .orElseThrow(EntityNotFoundException::new);

        boolean testWasStarted = historyRepository.existsByUser_IdAndTest_IdAndTestProgressStatus(
                user.getId(),
                testId,
                TestProgressStatus.STARTED
        );

        if (!testWasStarted)
            throw new TestHasNotStartedException();

        long result = calculateResult(test, selectedAnswersToQuestions);
        Timestamp completionTime = new Timestamp(System.currentTimeMillis());
        historyRepository.updateHistoryOfTestForFinishedTest(
                completionTime,
                (int) result,
                user, test
        );

        HistoryOfTest record = historyRepository
                .findByUser_IdAndCompletionTime(user.getId(), completionTime)
                .orElseThrow(EntityNotFoundException::new);

        log.info("The test(id:{}) was finished successfully", testId);
        return HistoryOfTestMapper.INSTANCE.mapToHistoryOfTestDTO(record);
    }

    private List<Test> sortTestList(String sorting) {
        if (sorting != null) {
            // Title Up
            if (sorting.equals(TestSorting.BY_TITLE_UP.name())) {
                return testRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
            }
            // Title Down
            if (sorting.equals(TestSorting.BY_TITLE_DOWN.name())) {
                return testRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
            }
            // Difficulty Up
            if (sorting.equals(TestSorting.BY_DIFFICULTY_UP.name())) {
                return testRepository.findAll(Sort.by(Sort.Direction.ASC, "testDifficulty"));
            }
            // Difficulty Down
            if (sorting.equals(TestSorting.BY_DIFFICULTY_DOWN.name())) {
                return testRepository.findAll(Sort.by(Sort.Direction.DESC, "testDifficulty"));
            }
            // Count of Questions Up
            if (sorting.equals(TestSorting.BY_COUNT_OF_QUESTIONS_UP.name())) {
                return testRepository.findAll(Sort.by(Sort.Direction.ASC, "questions"));
            }
            // Count of Questions Down
            if (sorting.equals(TestSorting.BY_COUNT_OF_QUESTIONS_DOWN.name())) {
                return testRepository.findAll(Sort.by(Sort.Direction.DESC, "questions"));
            }
        }

        throw new ServiceException("Sorting value should be correct");
    }

    private List<Test> filterBySubjectAndSort(String subject, String sorting) {
        if (sorting != null && subject != null) {
            // Title Up
            if (sorting.equals(TestSorting.BY_TITLE_UP.name())) {
                return testRepository.findAllBySubject_NameOrderByTitleAsc(subject);
            }
            // Title Down
            if (sorting.equals(TestSorting.BY_TITLE_DOWN.name())) {
                return testRepository.findAllBySubject_NameOrderByTitleDesc(subject);
            }
            // Difficulty Up
            if (sorting.equals(TestSorting.BY_DIFFICULTY_UP.name())) {
                return testRepository.findAllBySubject_NameOrderByTestDifficultyAsc(subject);
            }
            // Difficulty Down
            if (sorting.equals(TestSorting.BY_DIFFICULTY_DOWN.name())) {
                return testRepository.findAllBySubject_NameOrderByTestDifficultyDesc(subject);
            }
            // Count of Questions Up
            if (sorting.equals(TestSorting.BY_COUNT_OF_QUESTIONS_UP.name())) {
                return testRepository.findAllBySubject_NameOrderByQuestionListAsc(subject);
            }
            // Count of Questions Down
            if (sorting.equals(TestSorting.BY_COUNT_OF_QUESTIONS_DOWN.name())) {
                return testRepository.findAllBySubject_NameOrderByQuestionListDesc(subject);
            }
        }

        throw new ServiceException("Sorting and Subject should be correct");
    }

    private long calculateResult(Test test, List<AnswersToQuestionDTO> selectedAnswersToQuestions) {

        int countOfCorrectChoices = 0;

        for (AnswersToQuestionDTO choice : selectedAnswersToQuestions) {
            if (isCorrectSelectedAnswer(choice))
                countOfCorrectChoices++;
        }

        int countOfQuestionsInTest = test.getQuestionList().size();
        long result = Math.round(countOfCorrectChoices * 100.0 / countOfQuestionsInTest);
        log.info("Calculating result of test was done successful with score: {}%", result);
        return result;
    }

    private boolean isCorrectSelectedAnswer(AnswersToQuestionDTO selectedAnswer) {

        Question question = questionRepository.findById(selectedAnswer.getQuestionId())
                .orElseThrow(EntityNotFoundException::new);
        List<Answer> rightAnswersInQuestion = filterRightAnswers(question.getAnswerList());

        List<Answer> allSelectedAnswers = mapToAnswerEntities(selectedAnswer.getSelectedAnswersIds());
        List<Answer> rightSelectedAnswers = filterRightAnswers(allSelectedAnswers);

        return rightAnswersInQuestion.equals(rightSelectedAnswers);
    }

    private List<Answer> filterRightAnswers(List<Answer> allAnswers) {
        return allAnswers.stream()
                .filter(answer -> answer.getAnswerStatus().equals(AnswerStatus.RIGHT))
                .toList();
    }

    private List<Answer> mapToAnswerEntities(Set<Long> answerIdSet) {
        return answerIdSet.stream().map(this::extractAnswerById).toList();
    }

    private Answer extractAnswerById(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(EntityNotFoundException::new);
    }

    private boolean someOfTestsHaveAlreadyStarted(Long userId) {
        return historyRepository.existsByUser_IdAndTestProgressStatus(
                userId,
                TestProgressStatus.STARTED
        );
    }

    private TestDTO getTestDtoWithoutAnswerStatuses(Test test) {
        TestDTO testDTO = TestMapper.INSTANCE.mapToTestDTO(test);
        testDTO.getQuestionList().stream()
                .flatMap(q -> q.getAnswerList().stream())
                .forEach(a -> a.setAnswerStatus(null));
        return testDTO;
    }
}
