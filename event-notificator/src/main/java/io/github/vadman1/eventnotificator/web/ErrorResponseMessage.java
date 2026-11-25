package io.github.vadman1.eventnotificator.web;

import java.time.LocalDateTime;

public record ErrorResponseMessage(String message, String detailedMessage, LocalDateTime dateTime) {
}
