package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.UserDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.exception.IncorrectEnteredDataException;
import com.mrochko.testingUA.exception.UserDoesNotHaveAccessException;
import com.mrochko.testingUA.mapper.UserMapper;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.UserRole;
import com.mrochko.testingUA.repository.UserRepository;
import com.mrochko.testingUA.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.existsByLoginOrEmail(userDTO.getLogin(), userDTO.getEmail())) {
            log.warn("Login or email has already exist");
            throw new IncorrectEnteredDataException("Login or email has already exist");
        }

        User user = UserMapper.INSTANCE.mapToUser(userDTO);
        user.setAccess(true);
        user = userRepository.save(user);
        log.info("New user was created successfully");
        return UserMapper.INSTANCE.mapToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        log.info("Successful getting a list of users from the repository");
        return UserMapper.INSTANCE.mapToListOfUsersDTO(userList);
    }

    @Override
    public UserDTO getUserByID(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User was not found"));
        log.info("Successful getting a user from the repository");
        return UserMapper.INSTANCE.mapToUserDTO(user);
    }

    @Override
    public void updateUser(long userId, UserDTO userDTO, UserDetails userDetails) {

        User authenticatedUser = userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User was not authenticated"));

        if (authenticatedUser.getUserRole().equals(UserRole.ROLE_STUDENT) &&
                authenticatedUser.getId() != userId) {
            throw new UserDoesNotHaveAccessException("Student does not have access to other users");
        }

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User was not found");
        }

        userRepository.updateUser(
                userDTO.getLogin(),
                userDTO.getUserRole(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getAccess(),
                userId
        );

        log.info("Successful updating a user in the repository");
    }

}
