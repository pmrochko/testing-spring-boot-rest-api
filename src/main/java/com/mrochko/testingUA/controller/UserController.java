package com.mrochko.testingUA.controller;

import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.UserDTO;
import com.mrochko.testingUA.dto.group.OnCreate;
import com.mrochko.testingUA.dto.group.OnUpdate;
import com.mrochko.testingUA.service.HistoryOfTestService;
import com.mrochko.testingUA.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final HistoryOfTestService historyService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Validated(OnCreate.class) UserDTO userDTO) {
        log.info("Creating a new user with email: {}", userDTO.getEmail());
        return userService.createUser(userDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        log.info("Getting a list of all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable @Positive Long userId) {
        log.info("Getting a user by id: {}", userId);
        return userService.getUserByID(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> updateUser(@PathVariable @Positive Long userId,
                                           @RequestBody @Validated(OnUpdate.class) UserDTO userDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Updating a user with id: {}", userId);
        userService.updateUser(userId, userDTO, userDetails);

        log.info("User with id {}, has been successfully updated", userId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @GetMapping("/{userId}/historyOfTests")
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryOfTestDTO> getAllHistoryOfTests(@PathVariable @Positive Long userId) {
        log.info("Getting a list of all history of tests for the user with id: {}", userId);
        return historyService.getHistoryOfTests(userId);
    }

}
