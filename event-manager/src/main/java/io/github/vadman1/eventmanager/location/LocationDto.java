package io.github.vadman1.eventmanager.location;

import jakarta.validation.constraints.*;

public record LocationDto(
        @Null
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String address,

        @Min(1)
        @Max(10000)
        @NotNull
        Integer capacity
) {
}