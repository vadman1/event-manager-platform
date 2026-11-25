package io.github.vadman1.eventnotificator.notification;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationDto> findUnread(@AuthenticationPrincipal Long userId) {
        return notificationService.findUnread(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@AuthenticationPrincipal Long userId, @Valid @RequestBody MarkAsReadRequest request) {
        notificationService.markAsRead(userId, request.notificationIds());
    }

    public record MarkAsReadRequest(@NotEmpty List<@NotNull Long> notificationIds) {
    }
}
