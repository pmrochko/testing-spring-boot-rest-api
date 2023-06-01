package com.mrochko.testingUA.util;

import com.mrochko.testingUA.dto.QuestionDTO;
import com.mrochko.testingUA.mapper.QuestionMapper;
import com.mrochko.testingUA.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public class QuestionDataTestUtil {

    public static final Long QUESTION_ID = 60L;
    public static final String QUESTION_TEXT = "Question Text";
    public static final int QUESTION_LIST_SIZE = 3;

    public static Question createQuestion() {
        return QuestionMapper.INSTANCE.mapToQuestion(createQuestionDTO());
    }

    public static QuestionDTO createQuestionDTO() {
        return QuestionDTO.builder()
                .id(QUESTION_ID)
                .questionText(QUESTION_TEXT)
                .answerList(AnswerDataTestUtil.createAnswerDtoList())
                .build();
    }

    public static List<QuestionDTO> createQuestionDtoList() {
        List<QuestionDTO> list = new ArrayList<>();
        for (int i = 0; i < QUESTION_LIST_SIZE; i++) {
            list.add(
                    QuestionDTO.builder()
                            .id(QUESTION_ID + i)
                            .questionText(QUESTION_TEXT + "_" + i)
                            .answerList(AnswerDataTestUtil.createAnswerDtoList())
                            .build()
            );
        }
        return list;
    }

    public static List<Question> createQuestionList() {
        return QuestionMapper.INSTANCE.mapToListOfQuestions(createQuestionDtoList());
    }

}
