package com.mrochko.testingUA.model;

import com.mrochko.testingUA.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "surname", length = 20)
    private String surname;

    @Column(name = "tel", unique = true, length = 15)
    private String tel;

    @Column(name = "access", nullable = false, length = 15)
    private Boolean access;

}
