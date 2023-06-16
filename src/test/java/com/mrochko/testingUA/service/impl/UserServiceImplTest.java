package com.mrochko.testingUA.service.impl;

import com.mrochko.testingUA.dto.UserDTO;
import com.mrochko.testingUA.exception.EntityNotFoundException;
import com.mrochko.testingUA.exception.IncorrectEnteredDataException;
import com.mrochko.testingUA.exception.UserDoesNotHaveAccessException;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.repository.UserRepository;
import com.mrochko.testingUA.util.UserDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void createUser() {
        User testUser = UserDataTestUtil.createUser();
        UserDTO testUserDTO = UserDataTestUtil.createUserDTO();
        when(userRepository.save(any())).thenReturn(testUser);

        UserDTO userDTO = userService.createUser(testUserDTO);

        assertEqualsUserProperties(userDTO, testUser);
    }

    @Test
    void createUser_LoginOrEmailNotUnique() {
        UserDTO userDTO = UserDataTestUtil.createUserDTO();
        when(userRepository.existsByLoginOrEmail(anyString(), anyString())).thenReturn(true);

        assertThrows(IncorrectEnteredDataException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void getAllUsers() {
        List<User> testUserList = UserDataTestUtil.createUserList();
        when(userRepository.findAll()).thenReturn(testUserList);

        List<UserDTO> userDtoList = userService.getAllUsers();
        for (int i = 0; i < testUserList.size(); i++) {
            User testUser = testUserList.get(i);
            UserDTO userDTO = userDtoList.get(i);

            assertEqualsUserProperties(userDTO, testUser);
        }
    }

    @Test
    void getUserById() {
        User testUser = UserDataTestUtil.createUser();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        UserDTO userDTO = userService.getUserByID(123L);

        assertEqualsUserProperties(userDTO, testUser);
    }

    @Test
    void getUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByID(123L));
    }

    @Test
    void updateUser() {
        User testUser = UserDataTestUtil.createUser();
        UserDTO testUserDTO = UserDataTestUtil.createUserDTO();
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).updateUser(anyString(), any(), anyString(), anyString(), any(), anyLong());

        userService.updateUser(testUser.getId(), testUserDTO,
                UserDataTestUtil.createUserDetails());

        verify(userRepository, times(1))
                .updateUser(testUser.getLogin(), testUser.getUserRole(), testUser.getName(),
                        testUser.getSurname(), testUser.getAccess(), testUser.getId());
    }

    @Test
    void updateUser_UserDoesNotHaveAccess() {
        User testUser = UserDataTestUtil.createUser();
        UserDTO testUserDTO = UserDataTestUtil.createUserDTO();
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));

        assertThrows(UserDoesNotHaveAccessException.class,
                () -> userService.updateUser(testUserDTO.getId() + 3,
                        testUserDTO, UserDataTestUtil.createUserDetails()));
    }

    @Test
    void updateUser_UserNotFound() {
        User testUser = UserDataTestUtil.createUser();
        UserDTO testUserDTO = UserDataTestUtil.createUserDTO();
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(testUserDTO.getId(), testUserDTO, UserDataTestUtil.createUserDetails()));
    }

    private void assertEqualsUserProperties(UserDTO userDTO, User testUser) {
        assertThat(userDTO, allOf(
                hasProperty("id", equalTo(testUser.getId())),
                hasProperty("login", equalTo(testUser.getLogin())),
                hasProperty("email", equalTo(testUser.getEmail())),
                hasProperty("name", equalTo(testUser.getName())),
                hasProperty("surname", equalTo(testUser.getSurname())),
                hasProperty("tel", equalTo(testUser.getTel())),
                hasProperty("password", equalTo(testUser.getPassword())),
                hasProperty("userRole", equalTo(testUser.getUserRole()))
        ));
    }

}
