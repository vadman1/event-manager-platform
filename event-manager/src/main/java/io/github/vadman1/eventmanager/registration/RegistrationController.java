package io.github.vadman1.eventmanager.registration;

import io.github.vadman1.eventmanager.event.EventDto;
import io.github.vadman1.eventmanager.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{eventId}")
    public void register(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        registrationService.register(eventId, user);
    }

    @DeleteMapping("/cancel/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        registrationService.cancel(eventId, user);
    }

    @GetMapping("/my")
    public List<EventDto> findMyEvents(@AuthenticationPrincipal User user) {
        return registrationService.findMyEvents(user);
    }
}
