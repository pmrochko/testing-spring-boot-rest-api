package com.mrochko.testingUA.controller;

import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.service.AnswerService;
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
@RequestMapping("/api/v1/tests/questions")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerDTO createAnswerForQuestion(@Positive @PathVariable Long questionId,
                                             @Valid @RequestBody AnswerDTO answerDTO) {
        log.info("Creating a new answer for a question with id: {}", questionId);
        return answerService.createAnswer(questionId, answerDTO);
    }

    @GetMapping("/{questionId}/answers")
    @ResponseStatus(HttpStatus.OK)
    public List<AnswerDTO> getAllAnswersForQuestion(@Positive @PathVariable Long questionId) {
        log.info("Getting all answer for a question with id: {}", questionId);
        return answerService.getAllAnswers(questionId);
    }

    @DeleteMapping("/answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> deleteAnswer(@Positive @PathVariable Long answerId) {
        log.info("Deleting a answer with id: {}", answerId);
        answerService.deleteAnswer(answerId);

        log.info("Answer with id {}, has been successfully deleted", answerId);
        return new ResponseEntity<>(answerId, HttpStatus.OK);
    }

    @PutMapping("/answers/{answerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updateAnswer(@Positive @PathVariable Long answerId,
                                  @Valid @RequestBody AnswerDTO answerDTO) {
        log.info("Updating a answer with id: {}", answerDTO);
        answerService.updateAnswer(answerId, answerDTO);

        log.info("Answer with id {}, has been successfully updated", answerId);
        return new ResponseEntity<>(answerId, HttpStatus.OK);
    }

}
