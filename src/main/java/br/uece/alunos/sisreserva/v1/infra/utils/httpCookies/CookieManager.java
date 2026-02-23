package br.uece.alunos.sisreserva.v1.infra.utils.httpCookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private static final int ACCESS_TOKEN_MAX_AGE  = 15 * 60;
    private static final int REFRESH_TOKEN_MAX_AGE = 30 * 24 * 60 * 60;

    @Value("${cookie.secure:true}")
    private boolean secureCookie;

    /**
     * Writes the accessToken httpOnly cookie to the response.
     * Scoped to Path=/ so it is sent on every request.
     */
    public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        String cookie = buildCookieHeader(
                "accessToken",
                accessToken,
                "/",
                ACCESS_TOKEN_MAX_AGE
        );
        response.addHeader("Set-Cookie", cookie);
    }

    /**
     * Expires the accessToken cookie by setting Max-Age=0.
     */
    public void removeAccessTokenCookie(HttpServletResponse response) {
        String cookie = buildCookieHeader("accessToken", "", "/", 0);
        response.addHeader("Set-Cookie", cookie);
    }

    /**
     * Writes the refreshToken httpOnly cookie to the response.
     * Scoped to Path=/auth/refresh so the browser only sends it
     * to the refresh endpoint, not to every API call.
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        String cookie = buildCookieHeader(
                "refreshToken",
                refreshToken,
                "/auth/refresh",
                REFRESH_TOKEN_MAX_AGE
        );
        response.addHeader("Set-Cookie", cookie);
    }

    /**
     * Expires the refreshToken cookie by setting Max-Age=0.
     */
    public void removeRefreshTokenCookie(HttpServletResponse response) {
        String cookie = buildCookieHeader("refreshToken", "", "/auth/refresh", 0);
        response.addHeader("Set-Cookie", cookie);
    }

    public void removeAllAuthCookies(HttpServletResponse response) {
        removeAccessTokenCookie(response);
        removeRefreshTokenCookie(response);
    }

    /**
     * Reads the refreshToken cookie value from the incoming request.
     *
     * @return the raw refresh JWT string, or null if the cookie is absent
     */
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, "refreshToken");
    }

    /**
     * Reads the accessToken cookie value from the incoming request.
     *
     * @return the raw access JWT string, or null if the cookie is absent
     */
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getCookieValue(request, "accessToken");
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Builds a Set-Cookie header string with HttpOnly, SameSite=Lax,
     * and conditionally Secure.
     */
    private String buildCookieHeader(String name, String value, String path, int maxAge) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value).append("; ");
        sb.append("Path=").append(path).append("; ");
        sb.append("Max-Age=").append(maxAge).append("; ");
        sb.append("HttpOnly; ");
        sb.append("SameSite=Lax");

        if (secureCookie) {
            sb.append("; Secure");
        }

        return sb.toString();
    }
}
