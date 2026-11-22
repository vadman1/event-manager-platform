package io.github.vadman1.eventmanager.registration;

import io.github.vadman1.eventmanager.event.EventEntity;
import io.github.vadman1.eventmanager.user.UserEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "registration")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private Timestamp createdAt;

    public RegistrationEntity() {
    }

    public RegistrationEntity(EventEntity event, UserEntity user, Timestamp createdAt) {
        this.event = event;
        this.user = user;
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
