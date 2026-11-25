package io.github.vadman1.eventmanager.event;

import io.github.vadman1.eventcommon.ChangeItem;
import io.github.vadman1.eventcommon.EventChangeKafkaMessage;
import io.github.vadman1.eventmanager.registration.RegistrationRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class EventChangePublisher {
    private final RegistrationRepository registrations;
    private final ApplicationEventPublisher publisher;

    public EventChangePublisher(RegistrationRepository registrations, ApplicationEventPublisher publisher) {
        this.registrations = registrations;
        this.publisher = publisher;
    }

    public void publish(EventDto before, EventDto after, Long changedById) {
        var changes = new ArrayList<ChangeItem>();
        addChange(changes, "name", before.name(), after.name());
        addChange(changes, "date", before.date().toInstant().toString(), after.date().toInstant().toString());
        addChange(changes, "duration", before.duration(), after.duration());
        addChange(changes, "cost", before.cost(), after.cost());
        addChange(changes, "maxPlaces", before.maxPlaces(), after.maxPlaces());
        addChange(changes, "locationId", before.locationId(), after.locationId());
        addChange(changes, "status", before.status().name(), after.status().name());

        if (!changes.isEmpty()) {
            publisher.publishEvent(new EventChangeKafkaMessage(
                    UUID.randomUUID(), "EVENT_UPDATED", after.id(), OffsetDateTime.now(),
                    after.ownerId(), changedById, registrations.findSubscriberIds(after.id()), List.copyOf(changes)
            ));
        }
    }

    private void addChange(List<ChangeItem> changes, String field, Object before, Object after) {
        if (!Objects.equals(before, after)) {
            changes.add(new ChangeItem(field, before, after));
        }
    }
}
