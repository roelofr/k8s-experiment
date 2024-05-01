package dev.roelofr.k8s.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {
    public Integer id;
    public String name;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static User userFromName(String name) {
        return User.builder()
            .name(name)
            .build();
    }
}
