package dziedzic.dev.interview_backend.auth.service;

import dziedzic.dev.interview_backend.auth.model.Token;
import dziedzic.dev.interview_backend.auth.model.User;
import dziedzic.dev.interview_backend.auth.repository.TokenRepository;
import dziedzic.dev.interview_backend.auth.security.JWTUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JWTUtils jwtUtils;
    private final TokenRepository tokenRepository;
    private final UserService userService;

    public boolean isTokenValidAndUnrevoked(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    public void deleteAllUserTokens(User user) {
        tokenRepository.deleteAll(getUserTokens(user.getId()));
    }

    public List<Token> getUserTokens(UUID userId) {
        return tokenRepository.findAllValidTokenByUser(userId);
    }

    public void revokeAllUserTokens(User user) {
        List<Token> userTokens = getUserTokens(user.getId());
        userTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(userTokens);
    }

    @Transactional
    public String regenerateAccessToken(UserDetails userDetails) {

        User user = userService.getByUsername(userDetails.getUsername());

        String newAccessToken = jwtUtils.generateToken(userDetails);

        Token newAccessTokenEntry = Token.builder()
                .user(user)
                .token(newAccessToken)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(newAccessTokenEntry);

        return newAccessToken;
    }
}
