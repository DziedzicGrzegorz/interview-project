package dziedzic.dev.interview_backend.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

public class CookieUtils {
    public static void attachAuthCookies(HttpServletResponse response, String sessionToken, String refreshToken) {


        ResponseCookie sessionTokenCookie = CookieUtils.generateCookie("access_token",
                sessionToken,
                //5 minutes
                300L);
        response.addHeader(HttpHeaders.SET_COOKIE, sessionTokenCookie.toString());

        ResponseCookie refreshTokenCookie = CookieUtils.generateCookie("refresh_token",
                refreshToken,
                //7 days
                604800L);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }


    public static ResponseCookie generateCookie(String key, String value, Long durationSeconds) {
        return ResponseCookie.from(key, value)
                .maxAge(Math.round(durationSeconds))
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
    }

    public static ResponseCookie deleteCookie(String key) {
        return ResponseCookie.from(key)
                .maxAge(0)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();
    }

    public static void deleteAuthCookies(HttpServletResponse response) {
        ResponseCookie sessionTokenCookie = CookieUtils.deleteCookie("access_token");
        response.addHeader(HttpHeaders.SET_COOKIE, sessionTokenCookie.toString());

        ResponseCookie refreshTokenCookie = CookieUtils.deleteCookie("refresh_token");
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public static String getTokenFromCookies(HttpServletRequest request, String cookieName) {

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}