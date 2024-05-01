package dev.roelofr.k8s.controller;

import dev.roelofr.k8s.domain.User;
import dev.roelofr.k8s.dto.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

import static dev.roelofr.k8s.domain.User.userFromName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void getAll() {
        // mock
        given(userRepository.findAll()).willReturn(
            Arrays.asList(
                userFromName("Alice"),
                userFromName("Bob")
            )
        );

        var result = userController.getAll();
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));

        var resultBody = result.getBody();
        assertThat(resultBody, is(notNullValue()));
        assertThat(resultBody, arrayWithSize(2));

        assertThat(resultBody, arrayContaining(
            hasProperty("name", equalTo("Alice")),
            hasProperty("name", equalTo("Bob"))
        ));

        then(userRepository).should(times(1)).findAll();
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getOneExisting() {
        given(userRepository.findById(1)).willReturn(
            Optional.of(userFromName("Alice"))
        );

        var result = userController.getOne(1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));

        var resultBody = result.getBody();
        assertThat(resultBody, is(notNullValue()));
        assertThat(resultBody.getName(), is("Alice"));

        then(userRepository).should(times(1)).findById(1);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getOneMissing() {
        given(userRepository.findById(1)).willReturn(
            Optional.empty()
        );

        var result = userController.getOne(1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.NOT_FOUND));

        var resultBody = result.getBody();
        assertThat(resultBody, is(nullValue()));

        then(userRepository).should(times(1)).findById(1);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void create() {
        var inputUser = userFromName("Alice");

        given(userRepository.save(inputUser)).willReturn(
            User.builder().id(100).name("Alice").build()
        );

        var result = userController.create(inputUser);
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));

        var resultBody = result.getBody();
        assertThat(resultBody, is(notNullValue()));
        assertThat(resultBody.getId(), is(100));
        assertThat(resultBody.getName(), is("Alice"));

        then(userRepository).should(times(1)).save(inputUser);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateExisting() {
        var existingUser = userFromName("Alice");
        var inputUser = userFromName("Bob");
        var savedUser = User.builder().id(1).name("Bob").build();

        given(userRepository.findById(1))
            .willReturn(Optional.of(existingUser));

        given(userRepository.save(ArgumentMatchers.eq(1), ArgumentMatchers.same(inputUser)))
            .willReturn(savedUser);

        var result = userController.update(1, inputUser);
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));

        var resultBody = result.getBody();
        assertThat(resultBody, is(notNullValue()));
        assertThat(resultBody, is(savedUser));

        then(userRepository).should(times(1)).findById(1);
        then(userRepository).should(times(1)).save(1, inputUser);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void updateMissing() {
        given(userRepository.findById(1))
            .willReturn(Optional.empty());

        var result = userController.update(1, userFromName("Bob"));
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.NOT_FOUND));

        then(userRepository).should(times(1)).findById(1);
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void delete() {
        var id = 1;

        var result = userController.delete(id);
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.RESET_CONTENT));

        then(userRepository).should(times(1)).delete(id);
        then(userRepository).shouldHaveNoMoreInteractions();
    }
}
