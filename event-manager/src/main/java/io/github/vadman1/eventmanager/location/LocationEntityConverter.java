package io.github.vadman1.eventmanager.location;

import org.springframework.stereotype.Component;

@Component
public class LocationEntityConverter {

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity()
        );
    }

    public Location toDomain(LocationEntity location) {
        return new Location(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity()
        );
    }
}
