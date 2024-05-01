package dev.roelofr.k8s.dto;

import dev.roelofr.k8s.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class UserRepository {
    private final HashMap<Integer, User> users;
    private int nextId = 1;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public UserRepository(Collection<User> users) {
        var actualUsers = new HashMap<Integer, User>();

        for (var user : users) {
            user.setId(this.nextId++);
            actualUsers.put(user.getId(), user);
        }

        this.users = actualUsers;
    }

    public Collection<User> findAll() {
        return this.users.values();
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public User save(User user) {
        var created = User.builder()
            .id(nextId++)
            .name(user.getName())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        users.put(created.getId(), created);

        return created;
    }

    public User save(int id, User user) {
        var localUser = users.get(id);
        if (localUser == null)
            throw new IllegalArgumentException("User not found");

        localUser.setName(user.name);
        localUser.setUpdatedAt(LocalDateTime.now());

        return localUser;
    }

    public void delete(int id) {
        users.remove(id);
    }
}
