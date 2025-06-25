package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.LoginStatus;
import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.useCase.RegisterAuditLog;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;

@Component
public class RealizarLogin {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AtualizarUsuarioLoginErrado atualizarUsuarioLoginErrado;

    @Autowired
    private RegisterAuditLog registerAuditLog;

    @Transactional
    public AuthTokensDTO signIn(UsuarioLoginDTO data, HttpServletRequest request) {
        if (data.email().isEmpty() || data.senha().isEmpty()) {
            throw new ValidationException("Email e senha não podem ser vazios.");
        }

        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

        try {
            Authentication authentication = manager.authenticate(authenticationToken);

            Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

            usuarioAutenticado.resetAccessCount();

            String accessToken = tokenService.generateAccessToken(usuarioAutenticado);
            String refreshToken = null;
            if (usuarioAutenticado.isRefreshTokenEnabled()) {
                refreshToken = tokenService.generateRefreshToken(usuarioAutenticado);
            }

            registerAuditLog.logLogin(
                    data.email(),
                    request,
                    LoginStatus.SUCESSO,
                    request.getHeader("User-Agent")
            );

            return new AuthTokensDTO(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            handleFailedLogin(data.email(), request);
            throw new BadCredentialsException("Wrong login or password.");
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
