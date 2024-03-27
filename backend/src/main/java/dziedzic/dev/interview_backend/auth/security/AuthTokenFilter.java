package dziedzic.dev.interview_backend.auth.security;

import dziedzic.dev.interview_backend.auth.repository.TokenRepository;
import dziedzic.dev.interview_backend.auth.repository.UserRepository;
import dziedzic.dev.interview_backend.auth.service.AuthService;
import dziedzic.dev.interview_backend.auth.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getCookies() == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = CookieUtils.getTokenFromCookies(request, "access_token");
        String refreshToken = CookieUtils.getTokenFromCookies(request, "refresh_token");

        boolean isAccessTokenExpired = jwtUtils.isTokenExpired(accessToken);

        if (accessToken != null && !accessToken.isEmpty() && !isAccessTokenExpired) {

            String username = jwtUtils.extractUsername(accessToken);

            if (username == null) {
                throw new ServletException("Invalid access token");
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
            }

            //@TODO if database droped but user has tokens then then it will throw exception user not found
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            boolean isTokenValid = tokenService.isTokenValidAndUnrevoked(accessToken);

            if (isTokenValid && jwtUtils.isTokenUnexpiredAndMatchingUser(accessToken, userDetails)) {

                AuthService.authenticateUser(userDetails, request);
            }

            // If access token is expired, but refresh token is valid
        } else {
            boolean isRefreshExpired = jwtUtils.isTokenExpired(refreshToken);

            if (refreshToken == null || refreshToken.isEmpty() || isRefreshExpired) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtUtils.extractUsername(refreshToken);

            if (username == null) {
                throw new ServletException("Invalid refresh token");
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
            }

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            boolean isRefreshTokenValid = tokenService.isTokenValidAndUnrevoked(refreshToken);

            if (!isRefreshTokenValid || !jwtUtils.isTokenUnexpiredAndMatchingUser(refreshToken, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            String newAccessToken = tokenService.regenerateAccessToken(userDetails);

            AuthService.authenticateUser(userDetails, request);

            CookieUtils.attachAuthCookies(response, newAccessToken, refreshToken);
        }

        filterChain.doFilter(request, response);
    }
}