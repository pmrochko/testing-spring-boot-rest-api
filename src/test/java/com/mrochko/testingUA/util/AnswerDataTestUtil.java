package com.mrochko.testingUA.util;

import com.mrochko.testingUA.dto.AnswerDTO;
import com.mrochko.testingUA.mapper.AnswerMapper;
import com.mrochko.testingUA.model.Answer;
import com.mrochko.testingUA.model.enums.AnswerStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public class AnswerDataTestUtil {

    public static final Long ANSWER_ID = 1345L;
    public static final AnswerStatus ANSWER_STATUS = AnswerStatus.RIGHT;
    public static final String ANSWER_TEXT = "Answer Text";
    public static final int ANSWER_LIST_SIZE = 4;

    public static AnswerDTO createAnswerDTO() {
        return AnswerDTO.builder()
                .id(ANSWER_ID)
                .answerStatus(ANSWER_STATUS)
                .answerText(ANSWER_TEXT)
                .build();
    }

    public static Answer createAnswer() {
        return AnswerMapper.INSTANCE.mapToAnswer(createAnswerDTO());
    }

    public static List<AnswerDTO> createAnswerDtoList() {
        List<AnswerDTO> list = new ArrayList<>();
        for (int i = 0; i < ANSWER_LIST_SIZE; i++) {
            list.add(
                    AnswerDTO.builder()
                            .id(ANSWER_ID + i)
                            .answerStatus(ANSWER_STATUS)
                            .answerText(ANSWER_TEXT + "_" + i)
                            .build()
            );
        }
        return list;
    }

    public static List<Answer> createAnswerList() {
        return AnswerMapper.INSTANCE.mapToListOfAnswers(createAnswerDtoList());
    }

}
