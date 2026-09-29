package io.github.vadman1.eventmanager.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistration userRegistration) {
        if (userRepository.existsByLogin(userRegistration.login())) {
            throw new IllegalArgumentException("Username already taken");
        }

        var hashedPassword = passwordEncoder.encode(userRegistration.password());
        var userToSave = new UserEntity(
                null,
                userRegistration.login(),
                hashedPassword,
                userRegistration.age(),
                UserRole.USER.name()
        );

        var saved = userRepository.save(userToSave);

        return mapToDomain(saved);
    }

    public User findByLogin(String login) {
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapToDomain(user);
    }

    public boolean existByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public User findById(Long id) {
         var user = userRepository.findById(id)
                 .orElseThrow(() -> new EntityNotFoundException("User not found"));

         return mapToDomain(user);
    }

    private static User mapToDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getLogin(),
                entity.getAge(),
                entity.getPasswordHash(),
                UserRole.valueOf(entity.getRole())
        );
    }
}
