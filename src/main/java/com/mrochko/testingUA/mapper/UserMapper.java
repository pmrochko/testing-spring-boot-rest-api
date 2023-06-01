package com.mrochko.testingUA.mapper;

import com.mrochko.testingUA.dto.UserDTO;
import com.mrochko.testingUA.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO mapToUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    User mapToUser(UserDTO userDTO);

    List<UserDTO> mapToListOfUsersDTO(List<User> userList);
    List<User> mapToListOfUsers(List<UserDTO> userDtoList);

}
