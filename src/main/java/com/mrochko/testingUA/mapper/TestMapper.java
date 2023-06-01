package com.mrochko.testingUA.mapper;

import com.mrochko.testingUA.dto.TestDTO;
import com.mrochko.testingUA.model.Test;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(source = "subject.id", target = "subjectId")
    TestDTO mapToTestDTO(Test test);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "subjectId", target = "subject.id")
    })
    Test mapToTest(TestDTO testDTO);

    List<TestDTO> mapToListOfTestsDTO(List<Test> testList);
    List<Test> mapToListOfTests(List<TestDTO> testDtoList);

}
