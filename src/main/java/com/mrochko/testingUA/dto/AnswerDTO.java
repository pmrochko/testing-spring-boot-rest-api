package com.mrochko.testingUA.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrochko.testingUA.model.Answer;
import com.mrochko.testingUA.model.enums.AnswerStatus;
import com.mrochko.testingUA.validation.ValueOfEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * A DTO for the {@link Answer} entity
 * @author Pavlo Mrochko
 */
@Data
@Builder
@JsonInclude(NON_NULL)
public class AnswerDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ValueOfEnum(enumClass = AnswerStatus.class)
    private AnswerStatus answerStatus;

    @NotBlank
    private String answerText;

}
