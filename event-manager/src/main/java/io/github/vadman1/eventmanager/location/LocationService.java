package io.github.vadman1.eventmanager.location;

import io.github.vadman1.eventmanager.event.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConverter entityConverter;
    private final EventRepository eventRepository;

    public LocationService(
            LocationRepository locationRepository,
            LocationEntityConverter entityConverter,
            EventRepository eventRepository
    ) {
        this.locationRepository = locationRepository;
        this.entityConverter = entityConverter;
        this.eventRepository = eventRepository;
    }

    public Location createLocation(Location location) {
        LocationEntity locationToSave = entityConverter.toEntity(location);

        return entityConverter.toDomain(
                locationRepository.save(locationToSave)
        );
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(entityConverter::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Not found location by id=%s"
                        .formatted(id)));
    }

    @Transactional
    public Location updateLocation(Long id, Location locationToUpdate) {
        var location = findForUpdate(id);
        if (locationToUpdate.capacity() < location.getCapacity()
                && eventRepository.existsByLocationIdAndMaxPlacesGreaterThan(id, locationToUpdate.capacity())) {
            throw new IllegalArgumentException("Location capacity is below an existing event limit");
        }
        location.setName(locationToUpdate.name());
        location.setAddress(locationToUpdate.address());
        location.setCapacity(locationToUpdate.capacity());
        return entityConverter.toDomain(location);
    }

    @Transactional
    public void deleteLocation(Long id) {
        var location = findForUpdate(id);
        if (eventRepository.existsByLocationId(id)) {
            throw new IllegalArgumentException("Cannot delete a location with events");
        }
        locationRepository.delete(location);
    }

    private LocationEntity findForUpdate(Long id) {
        return locationRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found location by id=" + id));
    }
}
