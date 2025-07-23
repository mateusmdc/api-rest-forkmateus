package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
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

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var refreshToken = cookieManager.getRefreshTokenFromCookie(request);

        if (refreshToken != null && tokenService.isRefreshTokenValid(refreshToken)) {
            var decoded = tokenService.parseClaims(refreshToken);
            var refreshId = decoded.getClaim("refreshId").asString();

            refreshTokenLogService.revogarPorRefreshTokenId(refreshId);
        }

        cookieManager.removeRefreshTokenCookie(response);
    }
}