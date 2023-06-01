package com.mrochko.testingUA.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.model.enums.TestProgressStatus;
import com.mrochko.testingUA.validation.ValueOfEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

/**
 * A DTO for the {@link HistoryOfTest} entity
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class HistoryOfTestDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private String studentName;

    @NotNull
    private String testTitle;

    @NotNull
    private Timestamp completionTime;

    @ValueOfEnum(enumClass = TestProgressStatus.class)
    private TestProgressStatus testProgressStatus;

    @PositiveOrZero
    private Integer resultInPercent;

}
