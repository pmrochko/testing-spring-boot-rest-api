package com.mrochko.testingUA.mapper;

import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionDTO mapToQuestionDTO(Question question);
    @Mapping(target = "id", ignore = true)
    Question mapToQuestion(QuestionDTO questionDTO);

    List<Question> mapToListOfQuestions(List<QuestionDTO> questionDTOList);
    List<QuestionDTO> mapToListOfQuestionsDTO(List<Question> questionList);

}
