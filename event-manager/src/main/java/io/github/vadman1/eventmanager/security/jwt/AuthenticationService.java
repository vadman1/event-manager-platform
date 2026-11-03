package io.github.vadman1.eventmanager.security.jwt;

import io.github.vadman1.eventmanager.user.UserCredentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public String authenticateUser(UserCredentials userCredentials) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    userCredentials.login(),
                    userCredentials.password()
            )
        );

        return jwtTokenManager.generateToken(userCredentials.login());
    }
}
