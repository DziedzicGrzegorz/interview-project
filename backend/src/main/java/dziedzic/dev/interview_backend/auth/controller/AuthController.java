package dziedzic.dev.interview_backend.auth.controller;

import dziedzic.dev.interview_backend.auth.dto.LoginRequest;
import dziedzic.dev.interview_backend.auth.dto.UserDto;
import dziedzic.dev.interview_backend.auth.service.AuthService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final Bucket bucket;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto requestBody, HttpServletResponse response) {
        if (consumeTokenFromBucket()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded. Try again later.");
        }

        authService.register(requestBody, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        if (consumeTokenFromBucket()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded. Try again later.");
        }
        authService.login(loginRequest, response);
        return ResponseEntity.ok().body("Login successful.");
    }

    private boolean consumeTokenFromBucket() {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        return !probe.isConsumed();
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok().body("Logout successful.");
    }
}
