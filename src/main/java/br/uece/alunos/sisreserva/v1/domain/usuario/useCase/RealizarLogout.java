package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioCache;
import br.uece.alunos.sisreserva.v1.infra.utils.httpCookies.CookieManager;
import br.uece.alunos.sisreserva.v1.service.RefreshTokenLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RealizarLogout {

    private final CookieManager cookieManager;
    private final TokenService tokenService;
    private final RefreshTokenLogService refreshTokenLogService;
    private final UsuarioCache usuarioCache;

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = cookieManager.getRefreshTokenFromCookie(request);
        if (refreshToken != null && tokenService.isRefreshTokenValid(refreshToken)) {
            var decoded   = tokenService.parseClaims(refreshToken);
            var refreshId = decoded.getClaim("refreshId").asString();
            refreshTokenLogService.revogarPorRefreshTokenId(refreshId);
        }

        String accessToken = cookieManager.getAccessTokenFromCookie(request);
        if (accessToken != null && tokenService.isAccessTokenValid(accessToken)) {
            String email = tokenService.getSubject(accessToken);
            usuarioCache.evict(email);
        }

        cookieManager.removeAllAuthCookies(response);
    }
}