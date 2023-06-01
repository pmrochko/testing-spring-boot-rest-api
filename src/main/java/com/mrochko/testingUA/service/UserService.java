package com.mrochko.testingUA.service;

import com.mrochko.testingUA.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByID(long id);

    void updateUser(long userId, UserDTO userDTO, UserDetails userDetails);

    List<UserDTO> getAllUsers();

}
