package com.mrochko.testingUA.controller;

import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/{testId}/question")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDTO createQuestionForTest(@Positive @PathVariable Long testId,
                                             @Valid @RequestBody QuestionDTO questionDTO) {
        log.info("Creating a new question for a test with id: {}", testId);
        return questionService.createQuestion(testId, questionDTO);
    }

    @GetMapping("/{testId}/question")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionDTO> getAllQuestionsForTest(@Positive @PathVariable Long testId) {
        log.info("Getting all questions for a test with id: {}", testId);
        return questionService.getAllQuestions(testId);
    }

    @DeleteMapping("/question/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> deleteQuestion(@Positive @PathVariable Long questionId) {
        log.info("Deleting a question with id: {}", questionId);
        questionService.deleteQuestion(questionId);

        log.info("Question with id {}, has been successfully deleted", questionId);
        return new ResponseEntity<>(questionId, HttpStatus.OK);
    }

    @PutMapping("/question/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updateQuestion(@Positive @PathVariable Long questionId,
                                      @Valid @RequestBody QuestionDTO questionDTO) {
        log.info("Updating a question with id: {}", questionDTO);
        questionService.updateQuestion(questionId, questionDTO);

        log.info("Question with id {}, has been successfully updated", questionId);
        return new ResponseEntity<>(questionId, HttpStatus.OK);
    }

}
