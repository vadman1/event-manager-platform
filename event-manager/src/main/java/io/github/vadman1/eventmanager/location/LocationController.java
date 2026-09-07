package io.github.vadman1.eventmanager.location;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;
    private final LocationDtoConverter dtoConverter;

    public LocationController(
            LocationService locationService,
            LocationDtoConverter dtoConverter
    ) {
        this.locationService = locationService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public List<LocationDto> getAllLocations() {
        log.info("Get request for get all locations");

        return locationService.getAllLocations()
                .stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationToCreate
    ) {
        log.info("Get request for create location: location={}", locationToCreate);

        var createdLocation = locationService.createLocation(
                dtoConverter.toDomain(locationToCreate)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoConverter.toDto(createdLocation));
    }

    @GetMapping("/{id}")
    public LocationDto findById(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Get request for find location by id: id={}", id);

        return dtoConverter.toDto(
                locationService.getLocationById(id)
        );
    }

    @PutMapping("/{id}")
    public LocationDto updateLocation(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid LocationDto locationToUpdate
    ) {
        log.info("Get request for update location: id={}, locationToUpdate={}", id, locationToUpdate);

        var updatedLocation = locationService.updateLocation(
                id,
                dtoConverter.toDomain(locationToUpdate)
        );

        return dtoConverter.toDto(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Get request for delete location by id: id={}", id);

        locationService.deleteLocation(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
