package com.mrochko.testingUA.util;

import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.mapper.HistoryOfTestMapper;
import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.model.Test;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.TestProgressStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public class HistoryOfTestDataTestUtil {

    public static final Timestamp RECORD_COMPLETION_TIME = new Timestamp(System.currentTimeMillis());
    public static final Integer START_RESULT = 0;
    public static final Integer FINISH_RESULT = 33;
    public static final TestProgressStatus START_PROGRESS_STATUS = TestProgressStatus.STARTED;
    public static final TestProgressStatus FINISH_PROGRESS_STATUS = TestProgressStatus.FINISHED;
    public static final int HISTORY_OF_TEST_LIST_SIZE = 4;

    public static HistoryOfTest createStartHistoryOfTestRecord(User user, Test test) {
        return HistoryOfTest.builder()
                .user(user)
                .test(test)
                .completionTime(RECORD_COMPLETION_TIME)
                .resultInPercent(START_RESULT)
                .testProgressStatus(START_PROGRESS_STATUS)
                .build();
    }

    public static HistoryOfTest createFinishHistoryOfTestRecord(User user, Test test) {
        return HistoryOfTest.builder()
                .user(user)
                .test(test)
                .completionTime(RECORD_COMPLETION_TIME)
                .resultInPercent(FINISH_RESULT)
                .testProgressStatus(FINISH_PROGRESS_STATUS)
                .build();
    }

    public static List<HistoryOfTest> createHistoryOfTestList() {
        List<HistoryOfTest> list = new ArrayList<>();
        for (int i = 0; i < HISTORY_OF_TEST_LIST_SIZE; i++) {
            list.add(
                    HistoryOfTest.builder()
                            .id((long) i + 1)
                            .user(UserDataTestUtil.createUser())
                            .test(TestDataTestUtil.createTest())
                            .completionTime(RECORD_COMPLETION_TIME)
                            .resultInPercent(FINISH_RESULT + i)
                            .testProgressStatus(FINISH_PROGRESS_STATUS)
                            .build()
            );
        }
        return list;
    }

    public static List<HistoryOfTestDTO> createHistoryOfTestDtoList() {
        return HistoryOfTestMapper.INSTANCE.mapToListDTO(createHistoryOfTestList());
    }

}
