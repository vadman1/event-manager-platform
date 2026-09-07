package io.github.vadman1.eventmanager.location;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity
) {
}
