package io.github.vadman1.eventmanager.event;

import io.github.vadman1.eventmanager.location.LocationEntity;
import io.github.vadman1.eventmanager.location.LocationRepository;
import io.github.vadman1.eventmanager.user.User;
import io.github.vadman1.eventmanager.user.UserRepository;
import io.github.vadman1.eventmanager.user.UserRole;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final EventConverter converter;

    public EventService(EventRepository eventRepository, LocationRepository locationRepository,
                        UserRepository userRepository, EventConverter converter) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Transactional
    public EventDto create(EventCreateRequestDto request, User user) {
        var location = findLocationForUpdate(request.locationId());
        checkCapacity(request.maxPlaces(), location);

        var event = new EventEntity();
        event.setName(request.name());
        event.setStartAt(request.date());
        event.setDurationMinutes(request.duration());
        event.setCost(request.cost());
        event.setMaxPlaces(request.maxPlaces());
        event.setOccupiedPlaces(0);
        event.setStatus(EventStatus.WAIT_START);
        event.setLocation(location);
        event.setOwner(userRepository.getReferenceById(user.id()));
        return converter.toDto(eventRepository.save(event));
    }

    public EventDto findById(Long eventId) {
        return eventRepository.findById(eventId)
                .map(converter::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
    }

    public List<EventDto> findMyEvents(User user) {
        return eventRepository.findAllByOwnerId(user.id()).stream().map(converter::toDto).toList();
    }

    public List<EventDto> search(EventSearchRequestDto filter) {
        return eventRepository.search(filter).stream().map(converter::toDto).toList();
    }

    @Transactional
    public EventDto update(Long eventId, EventUpdateRequestDto request, User user) {
        var event = findForUpdate(eventId);
        checkOwnerOrAdmin(event, user);

        Long locationId = request.locationId() != null ? request.locationId() : event.getLocation().getId();
        var location = findLocationForUpdate(locationId);
        int maxPlaces = request.maxPlaces() != null ? request.maxPlaces() : event.getMaxPlaces();
        checkCapacity(maxPlaces, location);
        if (request.maxPlaces() != null && maxPlaces <= event.getOccupiedPlaces()) {
            throw new IllegalArgumentException("The new limit must exceed the number of occupied places");
        }

        event.setLocation(location);
        event.setMaxPlaces(maxPlaces);
        if (request.name() != null) {
            event.setName(request.name());
        }
        if (request.date() != null) {
            event.setStartAt(request.date());
        }
        if (request.duration() != null) {
            event.setDurationMinutes(request.duration());
        }
        if (request.cost() != null) {
            event.setCost(request.cost());
        }
        return converter.toDto(event);
    }

    @Transactional
    public void cancel(Long eventId, User user) {
        var event = findForUpdate(eventId);
        checkOwnerOrAdmin(event, user);
        if (event.getStatus() != EventStatus.WAIT_START || !event.getStartAt().isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Only an upcoming event can be cancelled");
        }
        event.setStatus(EventStatus.CANCELLED);
    }

    @Transactional
    public void advanceStatus(Long eventId, OffsetDateTime now) {
        var event = findForUpdate(eventId);
        if (event.getStatus() == EventStatus.WAIT_START && !event.getStartAt().isAfter(now)) {
            event.setStatus(EventStatus.STARTED);
        } else if (event.getStatus() == EventStatus.STARTED
                && !event.getStartAt().plusMinutes(event.getDurationMinutes()).isAfter(now)) {
            event.setStatus(EventStatus.FINISHED);
        }
    }

    private EventEntity findForUpdate(Long eventId) {
        return eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
    }

    private LocationEntity findLocationForUpdate(Long locationId) {
        return locationRepository.findByIdForUpdate(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found: " + locationId));
    }

    private void checkCapacity(int maxPlaces, LocationEntity location) {
        if (maxPlaces > location.getCapacity()) {
            throw new IllegalArgumentException("The event limit exceeds location capacity");
        }
    }

    private void checkOwnerOrAdmin(EventEntity event, User user) {
        if (user.role() != UserRole.ADMIN && !event.getOwner().getId().equals(user.id())) {
            throw new AccessDeniedException("Only the event owner or an administrator can manage it");
        }
    }
}
