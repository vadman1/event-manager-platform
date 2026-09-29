package io.github.vadman1.eventmanager.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistration(
        @NotBlank
        @Size(min = 5)
        String login,

        @NotBlank
        @Size(min = 5)
        String password,

        @Min(0)
        Integer age
) {
}
