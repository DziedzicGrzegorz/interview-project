package dziedzic.dev.interview_backend.auth.service;

import dziedzic.dev.interview_backend.auth.dto.UserDto;
import dziedzic.dev.interview_backend.auth.model.User;
import dziedzic.dev.interview_backend.auth.repository.UserRepository;
import dziedzic.dev.interview_backend.auth.security.CookieUtils;
import dziedzic.dev.interview_backend.auth.security.JWTUtils;
import dziedzic.dev.interview_backend.exception.CustomExceptions.UserAlreadyExistsException;
import dziedzic.dev.interview_backend.exception.CustomExceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(UserDto::new).toList();
    }

    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }


    public User getByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("user not found!");
        }
        return user.get();
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("user already exists!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(UserDto userDto) {
        var existing = getByEmail(userDto.getEmail());

        existing.setUsername(userDto.getUsername());
        existing.setPassword(passwordEncoder.encode(userDto.getPassword()));
        existing.setEmail(userDto.getEmail());

        return userRepository.save(existing);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    /**
     * Get user by request or null if not found
     * This method was created to get user from cookies
     * and return null if user is not found
     * The main purpose of this method is to get user from access token
     * but if access token expire then we regenerate and attach him in authFilter but this request cookies are not updated, so we need to get user from refresh token
     */
    public User getUserByRequestOrNull(HttpServletRequest request) {
        String token = CookieUtils.getTokenFromCookies(request, "access_token");
        token = token == null ? CookieUtils.getTokenFromCookies(request, "refresh_token") : token;

        try {
            String username = jwtUtils.extractUsername(token);
            return getByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}
