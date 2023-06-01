package com.mrochko.testingUA.controller;

import com.mrochko.testingUA.dto.AnswersToQuestionDTO;
import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final TestService testService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TestDTO createTest(@Valid @RequestBody TestDTO testDTO) {
        log.info("Creating a new test with title: {}", testDTO.getTitle());
        return testService.createTest(testDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<TestDTO> getAllTests(@RequestParam(required = false) String sorting,
                                     @RequestParam(required = false) String subject) {
        log.info("Getting a list of all tests");
        return testService.getAllTests(sorting, subject);
    }

    @DeleteMapping("/{testId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> deleteTest(@Positive @PathVariable Long testId) {
        log.info("Deleting a test with id: {}", testId);
        testService.deleteTest(testId);

        log.info("Test with id {}, has been successfully deleted", testId);
        return new ResponseEntity<>(testId, HttpStatus.OK);
    }

    @PutMapping("/{testId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updateTest(@Positive @PathVariable Long testId,
                                           @Valid @RequestBody TestDTO testDTO) {
        log.info("Updating a test with id: {}", testId);
        testService.updateTest(testId, testDTO);

        log.info("Test with id {}, has been successfully updated", testId);
        return new ResponseEntity<>(testId, HttpStatus.OK);
    }

    @PostMapping("/{testId}/start")
    @ResponseStatus(HttpStatus.CREATED)
    public TestDTO startTest(@AuthenticationPrincipal UserDetails userDetails,
                             @Positive @PathVariable Long testId) {
        log.info("Starting a passing test with id: {}", testId);
        return testService.startTest(userDetails, testId);
    }

    @PutMapping("/{testId}/submit")
    @ResponseStatus(HttpStatus.OK)
    public HistoryOfTestDTO finishTest(@AuthenticationPrincipal UserDetails userDetails,
                                       @Positive @PathVariable Long testId,
                                       @Valid @RequestBody List<AnswersToQuestionDTO> answers) {
        log.info("Finishing a passing test with id: {}", testId);
        return testService.finishTest(userDetails, testId, answers);
    }

}
