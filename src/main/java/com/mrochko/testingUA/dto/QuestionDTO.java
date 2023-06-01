package com.mrochko.testingUA.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrochko.testingUA.model.Question;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * A DTO for the {@link Question} entity
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class QuestionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String questionText;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<AnswerDTO> answerList;

}
