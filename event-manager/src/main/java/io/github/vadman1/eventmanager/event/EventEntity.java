package io.github.vadman1.eventmanager.event;

import io.github.vadman1.eventmanager.location.LocationEntity;
import io.github.vadman1.eventmanager.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "event")
@Getter
@Setter
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private OffsetDateTime startAt;
    private Integer durationMinutes;
    private Integer cost;
    private Integer maxPlaces;
    private Integer occupiedPlaces;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id")
    private LocationEntity location;
}
