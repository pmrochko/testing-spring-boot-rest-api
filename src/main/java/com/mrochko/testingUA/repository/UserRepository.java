package com.mrochko.testingUA.repository;

import com.mrochko.testingUA.model.User;
import com.mrochko.testingUA.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Pavlo Mrochko
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    boolean existsByLoginOrEmail(String login, String email);

    @Transactional
    @Modifying
    @Query("update User u set u.login = ?1, " +
                             "u.userRole = ?2, " +
                             "u.name = ?3, " +
                             "u.surname = ?4, " +
                             "u.access = ?5" +
            " where u.id = ?6")
    void updateUser(String login,
                    UserRole userRole,
                    String name,
                    String surname,
                    Boolean access,
                    Long id);



}
