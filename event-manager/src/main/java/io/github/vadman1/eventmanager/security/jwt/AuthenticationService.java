package io.github.vadman1.eventmanager.security.jwt;

import io.github.vadman1.eventmanager.user.UserCredentials;
import io.github.vadman1.eventmanager.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager,
                                 UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }

    public String authenticateUser(UserCredentials userCredentials) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    userCredentials.login(),
                    userCredentials.password()
            )
        );

        return jwtTokenManager.generateToken(userService.findByLogin(userCredentials.login()));
    }
}
