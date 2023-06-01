package com.mrochko.testingUA.service;

import com.mrochko.testingUA.dto.HistoryOfTestDTO;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface HistoryOfTestService {

    List<HistoryOfTestDTO> getHistoryOfTests(long userId);

}
