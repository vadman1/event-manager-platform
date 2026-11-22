package io.github.vadman1.eventmanager.event;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class EventScheduler {
    private final EventRepository eventRepository;
    private final EventService eventService;

    public EventScheduler(EventRepository eventRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @Scheduled(fixedDelay = 10_000)
    public void advanceStatuses() {
        var now = OffsetDateTime.now();
        for (Long eventId : eventRepository.findDueEventIds(now)) {
            eventService.advanceStatus(eventId, now);
        }
    }
}
