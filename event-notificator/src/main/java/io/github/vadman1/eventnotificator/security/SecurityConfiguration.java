package io.github.vadman1.eventnotificator.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vadman1.eventnotificator.web.ErrorResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper mapper,
                                            @Value("${jwt.secret-key}") String secretKey) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests.requestMatchers("/notifications")
                        .hasAnyAuthority("USER", "ADMIN").anyRequest().denyAll())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, error) ->
                                writeError(mapper, response, HttpStatus.UNAUTHORIZED,
                                        "Необходима аутентификация", error.getMessage()))
                        .accessDeniedHandler((request, response, error) ->
                                writeError(mapper, response, HttpStatus.FORBIDDEN,
                                        "Доступ запрещён", error.getMessage())))
                .addFilterBefore(new JwtTokenFilter(secretKey), AnonymousAuthenticationFilter.class)
                .build();
    }

    private void writeError(ObjectMapper mapper, HttpServletResponse response, HttpStatus status,
                            String message, String detail) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), new ErrorResponseMessage(message, detail, LocalDateTime.now()));
    }
}
