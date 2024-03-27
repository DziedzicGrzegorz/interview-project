package dziedzic.dev.interview_backend.auth.service;

import dziedzic.dev.interview_backend.auth.dto.UserDto;
import dziedzic.dev.interview_backend.auth.exception.CustomExceptions.UserAlreadyExistsException;
import dziedzic.dev.interview_backend.auth.exception.CustomExceptions.UserNotFoundException;
import dziedzic.dev.interview_backend.auth.model.User;
import dziedzic.dev.interview_backend.auth.repository.UserRepository;
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
}
