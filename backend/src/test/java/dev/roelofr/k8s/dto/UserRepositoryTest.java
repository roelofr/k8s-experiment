package dev.roelofr.k8s.dto;

import dev.roelofr.k8s.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static dev.roelofr.k8s.domain.User.userFromName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserRepositoryTest {
    @Test
    void findAll() {
        var repository = new UserRepository(Arrays.asList(
            userFromName("Alice"),
            userFromName("Bob")
        ));

        var users = repository.findAll();

        // Check map size
        assertThat(users, hasSize(2));

        // Check first item
        var usersAsArray = users.toArray(new User[0]);
        assertThat(usersAsArray[0], allOf(
            hasProperty("name", equalTo("Alice")),
            hasProperty("id", equalTo(1))
        ));
    }

    @Test
    void findById() {
        var repository = new UserRepository(Arrays.asList(
            userFromName("Alice"),
            userFromName("Bob")
        ));

        var user = repository.findById(1);
        assertThat(user.isPresent(), is(true));

        var actualUser = user.get();
        assertThat(actualUser, allOf(
            hasProperty("name", equalTo("Alice")),
            hasProperty("id", equalTo(1))
        ));

        var missingUser = repository.findById(3);
        assertThat(missingUser.isEmpty(), is(true));
    }

    @Test
    void saveNew() {
        var repository = new UserRepository();
        var inputUser = userFromName("Alice");

        var savedUser = repository.save(inputUser);
        assertThat(savedUser, not(sameInstance(inputUser)));
        assertThat(savedUser.getName(), equalTo(inputUser.getName()));
        assertThat(savedUser.getId(), equalTo(1));
        assertThat(savedUser.getCreatedAt(), not(equalTo(nullValue())));
        assertThat(savedUser.getUpdatedAt(), not(equalTo(nullValue())));
    }

    @Test
    void saveExisting() {
        var seedDate = LocalDateTime.parse("2024-05-01T08:00:00");

        var repository = new UserRepository(Arrays.asList(
            User.builder().name("Alice").createdAt(seedDate).updatedAt(seedDate).build(),
            User.builder().name("Bob").createdAt(seedDate).updatedAt(seedDate).build()
        ));

        var inputUser = userFromName("Charlie");
        var savedUser = repository.save(1, inputUser);

        assertThat(savedUser, not(sameInstance(inputUser)));
        assertThat(savedUser.getName(), equalTo(inputUser.getName()));

        assertThat(savedUser.getId(), equalTo(1));
        assertThat(savedUser.getCreatedAt(), equalTo(seedDate));
        assertThat(savedUser.getUpdatedAt(), not(equalTo(seedDate)));
    }

    @Test
    void delete() {
        var repository = new UserRepository(Arrays.asList(
            userFromName("Alice"),
            userFromName("Bob")
        ));

        repository.delete(1);
        var users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users, everyItem(not(hasProperty("id", equalTo(1)))));

        var missingUser = repository.findById(1);
        assertThat(missingUser.isEmpty(), is(true));
    }
}
