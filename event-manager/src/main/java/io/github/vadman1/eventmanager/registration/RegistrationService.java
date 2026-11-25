package io.github.vadman1.eventmanager.registration;

import io.github.vadman1.eventmanager.event.*;
import io.github.vadman1.eventmanager.user.User;
import io.github.vadman1.eventmanager.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RegistrationService {
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventConverter converter;

    public RegistrationService(EventRepository eventRepository, RegistrationRepository registrationRepository,
                               UserRepository userRepository, EventConverter converter) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Transactional
    public void register(Long eventId, User user) {
        var event = findEventForUpdate(eventId);
        var now = OffsetDateTime.now();
        if (event.getStatus() != EventStatus.WAIT_START || !event.getStartAt().isAfter(now)) {
            throw new IllegalArgumentException("Registration is only open before the event starts");
        }
        if (event.getOwner().getId().equals(user.id())) {
            throw new IllegalArgumentException("The event organizer does not need to register");
        }
        if (registrationRepository.findByEventIdAndUserId(eventId, user.id()).isPresent()) {
            throw new IllegalArgumentException("User is already registered for this event");
        }
        if (event.getOccupiedPlaces() >= event.getMaxPlaces()) {
            throw new IllegalArgumentException("No places available");
        }

        registrationRepository.save(new RegistrationEntity(
                event, userRepository.getReferenceById(user.id()), Timestamp.from(now.toInstant())
        ));
        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
    }

    @Transactional
    public void cancel(Long eventId, User user) {
        var event = findEventForUpdate(eventId);
        if (event.getStatus() == EventStatus.STARTED || event.getStatus() == EventStatus.FINISHED
                || (event.getStatus() == EventStatus.WAIT_START
                    && !event.getStartAt().isAfter(OffsetDateTime.now()))) {
            throw new IllegalArgumentException("Registration cannot be cancelled after the event starts");
        }
        var registration = registrationRepository.findByEventIdAndUserId(eventId, user.id())
                .orElseThrow(() -> new EntityNotFoundException("Registration not found"));
        registrationRepository.delete(registration);
        event.setOccupiedPlaces(event.getOccupiedPlaces() - 1);
    }

    @Transactional(readOnly = true)
    public List<EventDto> findMyEvents(User user) {
        return registrationRepository.findEventsByUserId(user.id()).stream().map(converter::toDto).toList();
    }

    private EventEntity findEventForUpdate(Long eventId) {
        return eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
    }
}
