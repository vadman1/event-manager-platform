package io.github.vadman1.eventmanager.event;

import org.springframework.stereotype.Component;

@Component
public class EventConverter {
    public EventDto toDto(EventEntity event) {
        return new EventDto(
                event.getId(), event.getName(), event.getOwner().getId(), event.getMaxPlaces(),
                event.getOccupiedPlaces(), event.getStartAt(), event.getCost(),
                event.getDurationMinutes(), event.getLocation().getId(), event.getStatus()
        );
    }
}
