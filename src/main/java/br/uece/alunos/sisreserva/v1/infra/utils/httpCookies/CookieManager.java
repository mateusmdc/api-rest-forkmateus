package br.uece.alunos.sisreserva.v1.infra.utils.httpCookies;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    public HttpServletResponse addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie newCookie = new Cookie("refreshToken", refreshToken);
        newCookie.setHttpOnly(true); // Impede o acesso ao cookie via JavaScript no front
        newCookie.setSecure(false); // trocar para true se estiver em HTTPS
        newCookie.setPath("/"); // caminho do cookie
        newCookie.setMaxAge(30 * 24 * 60 * 60); // expiração do cookie para 30 dias
        response.addCookie(newCookie);
        return response;
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public HttpServletResponse removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("refreshToken", null);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setSecure(false); // trocar para true em produção com HTTPS
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
        return response;
    }

}
