package com.mrochko.testingUA.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrochko.testingUA.dto.HistoryOfTestDTO;
import com.mrochko.testingUA.dto.UserDTO;
import com.mrochko.testingUA.service.HistoryOfTestService;
import com.mrochko.testingUA.service.UserService;
import com.mrochko.testingUA.util.HistoryOfTestDataTestUtil;
import com.mrochko.testingUA.util.UserDataTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mrochko.testingUA.util.UserDataTestUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pavlo Mrochko
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private HistoryOfTestService historyService;

    private static final String USER_API_URL = "/api/v1/users";

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = UserDataTestUtil.createUserDTO();
        userDTO.setAccess(null); // in UserService "access" automatically set a "true" value
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        String jsonRequestBody = "{\"id\":34,\"login\":\"login\"," +
                "\"email\":\"my_email@gmail.com\",\"userRole\":\"ROLE_STUDENT\"," +
                "\"name\":\"Jack\",\"surname\":\"Strong\",\"tel\":\"+380123456789\"," +
                "\"access\":null,\"password\":\"TestPassword123\"," +
                "\"repeatPassword\":\"TestPassword123\"}";

        mockMvc.perform(post(USER_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.repeatPassword").doesNotExist())
                .andExpect(jsonPath("$.name").value(USER_NAME))
                .andExpect(jsonPath("$.login").value(USER_LOGIN));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = UserDataTestUtil.createUserDtoList();
        when(userService.getAllUsers()).thenReturn(users);

        ResultActions result = mockMvc.perform(get(USER_API_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(users.size())));

        for (int i = 0; i < users.size(); i++) {
            UserDTO user = users.get(i);
            result.andExpect(jsonPath("$[" + i + "].id").value(user.getId()))
                  .andExpect(jsonPath("$[" + i + "].login").value(user.getLogin()))
                  .andExpect(jsonPath("$[" + i + "].email").value(user.getEmail()))
                  .andExpect(jsonPath("$[" + i + "].userRole").value(user.getUserRole().name()))
                  .andExpect(jsonPath("$[" + i + "].name").value(user.getName()))
                  .andExpect(jsonPath("$[" + i + "].surname").value(user.getSurname()))
                  .andExpect(jsonPath("$[" + i + "].tel").value(user.getTel()));
        }
    }

    @Test
    void testGetUserById() throws Exception {
        UserDTO userDTO = UserDataTestUtil.createUserDTO();
        when(userService.getUserByID(anyLong())).thenReturn(userDTO);

        mockMvc.perform(get(USER_API_URL + "/{userId}", 21L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.login").value(userDTO.getLogin()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.userRole").value(userDTO.getUserRole().name()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.surname").value(userDTO.getSurname()))
                .andExpect(jsonPath("$.tel").value(userDTO.getTel()));
    }

    @Test
    @WithMockUser
    void testUpdateUser() throws Exception {
        UserDTO userDTO = UserDataTestUtil.createUserDTO();
        userDTO.setTel(null);
        userDTO.setEmail(null);
        doNothing().when(userService).updateUser(anyLong(), any(UserDTO.class), any(UserDetails.class));

        String jsonRequestBody = new ObjectMapper().writeValueAsString(userDTO);

        mockMvc.perform(put(USER_API_URL + "/{userId}", userDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(userDTO.getId().intValue()));
    }

    @Test
    void testGetAllHistoryOfTests() throws Exception {
        List<HistoryOfTestDTO> historyList = HistoryOfTestDataTestUtil.createHistoryOfTestDtoList();
        when(historyService.getHistoryOfTests(anyLong())).thenReturn(historyList);

        ResultActions result = mockMvc.perform(get(USER_API_URL + "/{userId}/historyOfTests", 111L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(historyList.size())));

        for (int i = 0; i < historyList.size(); i++) {
            HistoryOfTestDTO historyRecord = historyList.get(i);
            result.andExpect(jsonPath("$[" + i + "].id").value(historyRecord.getId()))
                  .andExpect(jsonPath("$[" + i + "].studentName")
                          .value(historyRecord.getStudentName()))
                  .andExpect(jsonPath("$[" + i + "].testTitle")
                            .value(historyRecord.getTestTitle()))
                  .andExpect(jsonPath("$[" + i + "].testProgressStatus")
                            .value(historyRecord.getTestProgressStatus().name()))
                  .andExpect(jsonPath("$[" + i + "].resultInPercent")
                            .value(historyRecord.getResultInPercent()));
        }
    }

}
