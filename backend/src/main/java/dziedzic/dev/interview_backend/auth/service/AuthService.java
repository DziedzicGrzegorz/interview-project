package dziedzic.dev.interview_backend.auth.service;

import dziedzic.dev.interview_backend.auth.dto.LoginRequest;
import dziedzic.dev.interview_backend.auth.dto.UserDto;
import dziedzic.dev.interview_backend.auth.event.UserRegisterEvent;
import dziedzic.dev.interview_backend.auth.model.Role;
import dziedzic.dev.interview_backend.auth.model.Token;
import dziedzic.dev.interview_backend.auth.model.User;
import dziedzic.dev.interview_backend.auth.repository.TokenRepository;
import dziedzic.dev.interview_backend.auth.repository.UserRepository;
import dziedzic.dev.interview_backend.auth.security.CookieUtils;
import dziedzic.dev.interview_backend.auth.security.JWTUtils;
import dziedzic.dev.interview_backend.exception.CustomExceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final TokenRepository tokenRepository;

    public void register(UserDto userDto, HttpServletResponse response) {
        User newUser = userService.createUser(new User(userDto));
        newUser.setRole(Role.USER);

        attachTokensToResponse(newUser, response);
        eventPublisher.publishEvent(new UserRegisterEvent(this, newUser));
    }

    public void login(LoginRequest request, HttpServletResponse response) {
        User user = loadUserByUsernameOrEmail(request.getUsername(), request.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));
        //@TODO only one user can be logged in at the same time
        revokeAndDeleteAllUserTokens(user);

        attachTokensToResponse(user, response);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String username = jwtUtils.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow();

        if (jwtUtils.isTokenUnexpiredAndMatchingUser(refreshToken, user)) {
            revokeAndDeleteAllUserTokens(user);
            attachTokensToResponse(user, response);
        }
    }

    private User loadUserByUsernameOrEmail(String username, String email) {
        if (username != null && !username.isBlank()) {
            return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username : " + username));
        } else if (email != null && !email.isBlank()) {
            return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));
        } else {
            throw new IllegalArgumentException("Username or email must be provided");
        }
    }

    private void attachTokensToResponse(User user, HttpServletResponse response) {
        String token = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        CookieUtils.attachAuthCookies(response, token, refreshToken);
        saveUserTokens(user, Arrays.asList(token, refreshToken));
    }

    private void saveUserTokens(User user, List<String> tokens) {
        List<Token> tokensToSave = tokens.stream()
                .map(token -> Token.builder().user(user).token(token).expired(false).revoked(false).build())
                .collect(Collectors.toList());
        tokenRepository.saveAll(tokensToSave);
    }

    private void revokeAndDeleteAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.deleteAll(validUserTokens);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    public static void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            //if user is here that means he is authenticated so we can get his username
            String accessToken = CookieUtils.getTokenFromCookies(request, "access_token");

            User user = userRepository.findByUsername(jwtUtils.extractUsername(accessToken)).
                    orElseThrow(() -> new UserNotFoundException("User not found"));

            revokeAllUserTokens(user);

        } catch (Exception e) {
            log.error("Error while logging out", e);
            return;
        }
        CookieUtils.deleteAuthCookies(response);
    }
}
