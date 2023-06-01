package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.mapper.HistoryOfTestMapper;
import com.mrochko.testingUA.model.HistoryOfTest;
import com.mrochko.testingUA.repository.HistoryOfTestRepository;
import com.mrochko.testingUA.repository.UserRepository;
import com.mrochko.testingUA.service.HistoryOfTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryOfTestServiceImpl implements HistoryOfTestService {

    private final HistoryOfTestRepository historyOfTestRepository;
    private final UserRepository userRepository;

    @Override
    public List<HistoryOfTestDTO> getHistoryOfTests(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User was not found");
        }
        List<HistoryOfTest> records = historyOfTestRepository.findAllByUser_Id(userId);
        log.info("All history of tests for the user was getting from repository successfully");
        return HistoryOfTestMapper.INSTANCE.mapToListDTO(records);
    }

}
