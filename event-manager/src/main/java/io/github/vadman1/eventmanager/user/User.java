package io.github.vadman1.eventmanager.user;

public record User(
        Long id,
        String login,
        Integer age,
        String passwordHash,
        UserRole role
) {
}
