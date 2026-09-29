package io.github.vadman1.eventmanager.user;

public record UserDTO(
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
