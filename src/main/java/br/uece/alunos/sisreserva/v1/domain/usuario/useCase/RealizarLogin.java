package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.LoginStatus;
import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.useCase.RegisterAuditLog;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioLoginDTO;
import br.uece.alunos.sisreserva.v1.service.RefreshTokenLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;

import java.time.ZoneId;

@Component
@AllArgsConstructor
public class RealizarLogin {
    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final AtualizarUsuarioLoginErrado atualizarUsuarioLoginErrado;
    private final RegisterAuditLog registerAuditLog;
    private final RefreshTokenLogService refreshTokenLogService;
    private final UsuarioValidator usuarioValidator;

    @Transactional
    public AuthTokensDTO login(UsuarioLoginDTO data, HttpServletRequest request) {
        usuarioValidator.validarCredenciaisPreenchidas(data.email(), data.senha());

        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        try {
            Authentication authentication = manager.authenticate(authenticationToken);

            Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

            usuarioAutenticado.resetAccessCount();

            var accessToken = tokenService.generateAccessToken(usuarioAutenticado);
            var refreshToken = tokenService.generateRefreshToken(usuarioAutenticado);

            var refreshClaims = tokenService.parseClaims(refreshToken);
            var refreshTokenId = refreshClaims.getClaim("refreshId").asString();
            var issuedAt = refreshClaims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            var expiresAt = refreshClaims.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            refreshTokenLogService.registrar(
                    usuarioAutenticado,
                    refreshTokenId,
                    issuedAt,
                    expiresAt,
                    request
            );

            registerAuditLog.logLogin(
                    data.email(),
                    request,
                    LoginStatus.SUCESSO,
                    request.getHeader("User-Agent")
            );

            return new AuthTokensDTO(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            handleFailedLogin(data.email(), request);
            throw new BadCredentialsException("Login ou senha errados.");
        }
    }

    @Transactional
    private void handleFailedLogin(String email, HttpServletRequest request) {
        atualizarUsuarioLoginErrado.updateFailedLogin(email);

        registerAuditLog.logLogin(
                email,
                request,
                LoginStatus.FALHA,
                request.getHeader("User-Agent")
        );
    }
}
