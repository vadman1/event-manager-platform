package io.github.vadman1.eventmanager.user;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (!userRepository.existsByLogin("admin")) {
            userRepository.save(
                    new UserEntity(
                            null,
                            "admin",
                            passwordEncoder.encode("admin"),
                            20,
                            UserRole.ADMIN.name()
                    )
            );
        }
    }
}
