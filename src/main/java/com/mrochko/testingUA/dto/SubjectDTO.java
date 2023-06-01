package com.mrochko.testingUA.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrochko.testingUA.model.Subject;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * A DTO for the {@link Subject} entity
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class SubjectDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

}
