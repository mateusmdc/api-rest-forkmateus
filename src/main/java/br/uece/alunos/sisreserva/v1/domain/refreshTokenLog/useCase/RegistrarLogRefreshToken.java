package br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.useCase;

import br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.RefreshTokenLog;
import br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.RefreshTokenLogRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RegistrarLogRefreshToken {

    private final RefreshTokenLogRepository repository;

    @Transactional
    public void registrar(Usuario usuario, String refreshTokenId, LocalDateTime issuedAt, LocalDateTime expiresAt, HttpServletRequest request) {
        var log = new RefreshTokenLog();

        log.setUsuario(usuario);
        log.setRefreshTokenId(refreshTokenId);
        log.setIssuedAt(issuedAt);
        log.setExpiresAt(expiresAt);
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setIpAddress(request.getRemoteAddr());
        log.setRevoked(false);

        repository.save(log);
    }
}
