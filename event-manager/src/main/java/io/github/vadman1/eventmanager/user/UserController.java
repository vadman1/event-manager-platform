package io.github.vadman1.eventmanager.user;

import io.github.vadman1.eventmanager.security.jwt.AuthenticationService;
import io.github.vadman1.eventmanager.security.jwt.JwtTokenResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> registerUser(
            @Valid @RequestBody UserRegistration userRegistration
    ) {
        log.info("Get request for sign-up: login={}", userRegistration.login());
        var user = userService.registerUser(userRegistration);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserDTO(
                        user.id(),
                        user.login(),
                        user.age(),
                        user.role()
                ));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @Valid @RequestBody UserCredentials userCredentials
    ) {
        log.info("Get request for sign-in: login={}", userCredentials.login());
        var token = authenticationService.authenticateUser(userCredentials);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        log.info("Get request for get user: id={}", userId);
        var user = userService.findById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserDTO(
                        user.id(),
                        user.login(),
                        user.age(),
                        user.role()
                ));
    }
}
