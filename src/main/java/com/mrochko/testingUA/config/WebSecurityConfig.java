package com.mrochko.testingUA.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

/**
 * @author Pavlo Mrochko
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("production")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("select login, password, access from users where login=?")
                .authoritiesByUsernameQuery("select login, user_role from users where login=?");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/user").permitAll()             // registration
                .antMatchers(HttpMethod.PUT, "/api/v1/user/{userId}")
                            .hasAnyRole("ADMIN", "STUDENT")                      // update user (additional security in UserService#updateUser)
                .antMatchers(HttpMethod.GET, "/api/v1/test").permitAll()              // list of tests
                .antMatchers("/api/v1/user/{userId}/historyOfTests").hasAnyRole("ADMIN", "STUDENT") // test passing
                .antMatchers("/api/v1/test/{testId}/start").hasAnyRole("ADMIN", "STUDENT") // test passing
                .antMatchers("/api/v1/test/{testId}/submit").hasAnyRole("ADMIN", "STUDENT") // test passing
                .antMatchers("/api/v1/user/**").hasRole("ADMIN")                      // actions with accounts
                .antMatchers("/api/v1/test/**").hasRole("ADMIN")                      // actions with editing a test
                .antMatchers("/api/v1/**").permitAll()                                // other endpoints
                .and().formLogin();

    }

}
