package com.mrochko.testingUA.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class AnswersToQuestionDTO {

    @NotNull
    private Long questionId;

    @NotNull
    private Set<Long> selectedAnswersIds;

}
