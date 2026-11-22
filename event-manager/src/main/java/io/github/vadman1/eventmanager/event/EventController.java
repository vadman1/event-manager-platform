package io.github.vadman1.eventmanager.event;

import io.github.vadman1.eventmanager.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@RequestBody @Valid EventCreateRequestDto request, @AuthenticationPrincipal User user) {
        return eventService.create(request, user);
    }

    @GetMapping("/{eventId}")
    public EventDto findById(@PathVariable Long eventId) {
        return eventService.findById(eventId);
    }

    @PutMapping("/{eventId}")
    public EventDto update(@PathVariable Long eventId, @RequestBody @Valid EventUpdateRequestDto request,
                           @AuthenticationPrincipal User user) {
        return eventService.update(eventId, request, user);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        eventService.cancel(eventId, user);
    }

    @PostMapping("/search")
    public List<EventDto> search(@RequestBody @Valid EventSearchRequestDto request) {
        return eventService.search(request);
    }

    @GetMapping("/my")
    public List<EventDto> findMyEvents(@AuthenticationPrincipal User user) {
        return eventService.findMyEvents(user);
    }
}
