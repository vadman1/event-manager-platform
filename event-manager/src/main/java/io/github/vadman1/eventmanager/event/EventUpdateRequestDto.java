package io.github.vadman1.eventmanager.event;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public record EventUpdateRequestDto(
        @Pattern(regexp = "(?s).*\\S.*") @Size(max = 255) String name,
        @Positive Integer maxPlaces,
        @Future OffsetDateTime date,
        @Positive Integer cost,
        @Min(30) Integer duration,
        @Positive Long locationId
) {
}
