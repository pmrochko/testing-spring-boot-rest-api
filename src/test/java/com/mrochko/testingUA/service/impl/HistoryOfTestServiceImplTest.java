package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.repository.HistoryOfTestRepository;
import com.mrochko.testingUA.repository.UserRepository;
import com.mrochko.testingUA.util.HistoryOfTestDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
class HistoryOfTestServiceImplTest {

    @InjectMocks
    HistoryOfTestServiceImpl historyOfTestService;

    @Mock
    UserRepository userRepository;
    @Mock
    HistoryOfTestRepository historyOfTestRepository;

    @Test
    void getHistoryOfTests() {
        List<HistoryOfTest> historyOfTestList = HistoryOfTestDataTestUtil.createHistoryOfTestList();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(historyOfTestRepository.findAllByUser_Id(anyLong())).thenReturn(historyOfTestList);

        List<HistoryOfTestDTO> historyOfTestDtoList = historyOfTestService.getHistoryOfTests(115L);

        for (int i = 0; i < historyOfTestList.size(); i++) {
            HistoryOfTest record = historyOfTestList.get(i);
            HistoryOfTestDTO recordDTO = historyOfTestDtoList.get(i);

            assertEqualsHistoryOfTestProperties(recordDTO, record);
        }
    }

    @Test
    void getHistoryOfTests_UserNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(EntityNotFoundException.class,
                () -> historyOfTestService.getHistoryOfTests(5L));
    }

    private void assertEqualsHistoryOfTestProperties(HistoryOfTestDTO recordDTO, HistoryOfTest record) {
        assertThat(recordDTO, allOf(
                hasProperty("id", equalTo(record.getId())),
                hasProperty("testProgressStatus", equalTo(record.getTestProgressStatus())),
                hasProperty("resultInPercent", equalTo(record.getResultInPercent()))
        ));
    }

}
