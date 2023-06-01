package com.mrochko.testingUA.mapper;

import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.model.HistoryOfTest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface HistoryOfTestMapper {

    HistoryOfTestMapper INSTANCE = Mappers.getMapper(HistoryOfTestMapper.class);

    @Mappings({
            @Mapping(source = "user.name", target = "studentName"),
            @Mapping(source = "test.title", target = "testTitle")
    })
    HistoryOfTestDTO mapToHistoryOfTestDTO(HistoryOfTest historyOfTest);

    List<HistoryOfTestDTO> mapToListDTO(List<HistoryOfTest> historyOfTestList);

}
