package br.uece.alunos.sisreserva.v1.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticateUserWithValidJwt authenticateUserWithValidJwt;
    private final UsuarioCache usuarioCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = getAccessToken(request);

        if (accessToken != null && tokenService.isAccessTokenValid(accessToken)) {
            authenticateUsuario(tokenService.getSubject(accessToken));
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUsuario(String subject) {
        Usuario usuario = usuarioCache.computeIfAbsent(
                subject,
                authenticateUserWithValidJwt::findUserAuthenticated
        );

        if (usuario != null) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    /**
     * @param request the incoming HTTP request
     * @return the raw JWT string, or null if neither source is present
     */
    private String getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}