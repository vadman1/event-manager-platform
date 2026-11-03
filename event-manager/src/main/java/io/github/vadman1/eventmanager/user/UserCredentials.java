package io.github.vadman1.eventmanager.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCredentials(
        @NotBlank
        @Size(min = 5)
        String login,

        @NotBlank
        @Size(min = 5)
        String password
) {
}
