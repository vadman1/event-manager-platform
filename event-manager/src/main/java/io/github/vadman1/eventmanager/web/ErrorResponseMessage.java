package io.github.vadman1.eventmanager.web;

import java.time.LocalDateTime;

public record ErrorResponseMessage (
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
}