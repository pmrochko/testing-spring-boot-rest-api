package com.mrochko.testingUA.util;

import com.mrochko.testingUA.dto.UserDTO;
import com.mrochko.testingUA.mapper.UserMapper;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Pavlo Mrochko
 */
public class UserDataTestUtil {

    public static final Long USER_ID = 34L;
    public static final String USER_LOGIN = "login";
    public static final String USER_EMAIL = "my_email@gmail.com";
    public static final String USER_NAME = "Jack";
    public static final String USER_SURNAME = "Strong";
    public static final String USER_TEL = "+380123456789";
    public static final UserRole USER_ROLE = UserRole.ROLE_STUDENT;
    private static final String USER_PASSWORD = "TestPassword123";
    public static final Boolean USER_ACCESS = Boolean.TRUE;
    public static final int USER_LIST_SIZE = 6;

    public static User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setName(USER_NAME);
        user.setSurname(USER_SURNAME);
        user.setTel(USER_TEL);
        user.setPassword(USER_PASSWORD);
        user.setUserRole(USER_ROLE);
        user.setAccess(USER_ACCESS);

        return user;
    }

    public static UserDTO createUserDTO() {
        UserDTO userDTO = UserMapper.INSTANCE.mapToUserDTO(createUser());
        userDTO.setRepeatPassword(USER_PASSWORD);

        return userDTO;
    }

    public static List<UserDTO> createUserDtoList() {
        List<UserDTO> userDtoList = new ArrayList<>();
        for (int i = 0; i < USER_LIST_SIZE; i++) {
            userDtoList.add(
                    UserDTO.builder()
                            .id(USER_ID + i)
                            .login(USER_LOGIN + "_" + i)
                            .email(USER_EMAIL + "_" + i)
                            .name(USER_NAME + "_" + i)
                            .surname(USER_SURNAME + "_" + i)
                            .tel(USER_TEL + "_" + i)
                            .userRole(USER_ROLE)
                            .build()
            );
        }
        return userDtoList;
    }

    public static List<User> createUserList() {
        return UserMapper.INSTANCE.mapToListOfUsers(createUserDtoList());
    }

    public static UserDetails createUserDetails() {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return USER_PASSWORD;
            }

            @Override
            public String getUsername() {
                return USER_LOGIN;
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return USER_ACCESS;
            }
        };
    }

}
