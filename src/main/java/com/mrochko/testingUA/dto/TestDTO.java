package com.mrochko.testingUA.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrochko.testingUA.model.Test;
import com.mrochko.testingUA.model.enums.TestDifficulty;
import com.mrochko.testingUA.validation.ValueOfEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

/**
 * A DTO for the {@link Test} entity
 * @author Pavlo Mrochko
 */
@Data
@Builder
@JsonInclude(NON_NULL)
public class TestDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private Long subjectId;

    @NotBlank
    private String title;

    private String description;

    @ValueOfEnum(enumClass = TestDifficulty.class)
    private TestDifficulty testDifficulty;

    @Positive
    private Integer minutes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<QuestionDTO> questionList;

}
