package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.LoginStatus;
import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.useCase.RegisterAuditLog;
import br.uece.alunos.sisreserva.v1.domain.credencialLdap.CredencialLdapRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.LoginInternoResultDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioLoginInternoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.infra.ldap.LdapAuthenticator;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioCache;
import br.uece.alunos.sisreserva.v1.service.RefreshTokenLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Component
@AllArgsConstructor
public class RealizarLoginLdap {

    private final LdapAuthenticator ldapAuthenticator;
    private final CredencialLdapRepository credencialLdapRepository;
    private final TokenService tokenService;
    private final RefreshTokenLogService refreshTokenLogService;
    private final RegisterAuditLog registerAuditLog;
    private final UsuarioCache usuarioCache;
    private final UsuarioValidator usuarioValidator;

    @Transactional
    public LoginInternoResultDTO login(UsuarioLoginInternoDTO data, HttpServletRequest request) {
        usuarioValidator.validarCredenciaisPreenchidas(data.ldapUsername(), data.senha());

        try {
            ldapAuthenticator.authenticate(data.ldapUsername(), data.senha());
        } catch (BadCredentialsException e) {
            registerAuditLog.logLogin(
                    data.ldapUsername(), request, LoginStatus.FALHA, request.getHeader("User-Agent")
            );
            throw new BadCredentialsException("Login ou senha errados.");
        }

        var credencialLdap = credencialLdapRepository.findByLdapUsernameWithUsuario(data.ldapUsername());

        if (credencialLdap.isEmpty()) {
            String onboardingToken = tokenService.generateOnboardingToken(data.ldapUsername());
            return new LoginInternoResultDTO.OnboardingRequerido(onboardingToken);
        }

        var usuario = credencialLdap.get().getUsuario();

        usuarioCache.evict(usuario.getEmail());

        var accessToken  = tokenService.generateAccessToken(usuario);
        var refreshToken = tokenService.generateRefreshToken(usuario);

        var refreshClaims   = tokenService.parseClaims(refreshToken);
        var refreshTokenId  = refreshClaims.getClaim("refreshId").asString();
        var issuedAt        = refreshClaims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        var expiresAtRaw    = refreshClaims.getExpiresAt();
        var expiresAt       = expiresAtRaw != null
                ? expiresAtRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                : null;

        refreshTokenLogService.registrar(usuario, refreshTokenId, issuedAt, expiresAt, request);
        registerAuditLog.logLogin(
                usuario.getEmail(), request, LoginStatus.SUCESSO, request.getHeader("User-Agent")
        );

        return new LoginInternoResultDTO.Autenticado(new AuthTokensDTO(accessToken, refreshToken));
    }
}