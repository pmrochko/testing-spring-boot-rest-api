package com.mrochko.testingUA.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrochko.testingUA.dto.group.OnCreate;
import com.mrochko.testingUA.dto.group.OnUpdate;
import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.UserRole;
import com.mrochko.testingUA.validation.ValidPassword;
import com.mrochko.testingUA.validation.ValueOfEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * A DTO for the {@link User} entity
 * @author Pavlo Mrochko
 */
@Data
@Builder
public class UserDTO {

    /* For change email, tel, password
       must be implemented others services.
       So, in a request email, tel, password fields must be null */

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "'login' shouldn't be absent in the request", groups = {OnCreate.class, OnUpdate.class})
    private String login;

    @Email(message = "'email' should be valid in the request", groups = OnCreate.class)
    @Null(message = "'email' should be absent in the request", groups = OnUpdate.class)
    private String email;

    @ValueOfEnum(enumClass = UserRole.class, groups = {OnCreate.class, OnUpdate.class})
    private UserRole userRole;

    @NotBlank(message = "'name' shouldn't be absent in the request", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "'surname' shouldn't be absent in the request", groups = {OnCreate.class, OnUpdate.class})
    private String surname;

    @NotBlank(message = "'tel' shouldn't be absent in the request", groups = OnCreate.class)
    @Null(message = "'tel' should be absent in the request", groups = OnUpdate.class)
    private String tel;

    @Null(message = "'access' should be absent in the request", groups = OnCreate.class)
    @NotNull(message = "'access' shouldn't be absent in the request", groups = OnUpdate.class)
    private Boolean access;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ValidPassword(message = "'password' should be valid in the request", groups = OnCreate.class)
    @Null(message = "'password' should be absent in the request", groups = OnUpdate.class)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Null(message = "'repeatPassword' should be absent in the request", groups = OnUpdate.class)
    private String repeatPassword;

    @AssertTrue(message = "'password' and 'repeatPassword' should match", groups = OnCreate.class)
    public boolean isPasswordsMatch() {
        return password != null && password.equals(repeatPassword);
    }

}
