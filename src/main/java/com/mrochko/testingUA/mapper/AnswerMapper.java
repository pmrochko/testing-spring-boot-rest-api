package com.mrochko.testingUA.mapper;

import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.model.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    AnswerDTO mapToAnswerDTO(Answer answer);
    @Mapping(target = "id", ignore = true)
    Answer mapToAnswer(AnswerDTO answerDTO);

    List<Answer> mapToListOfAnswers(List<AnswerDTO> answerDTOList);
    List<AnswerDTO> mapToListOfAnswersDTO(List<Answer> answerList);

}
