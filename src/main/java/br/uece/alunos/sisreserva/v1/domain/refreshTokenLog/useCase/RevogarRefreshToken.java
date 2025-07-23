package br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.useCase;

import br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.RefreshTokenLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class RevogarRefreshToken {

    private final RefreshTokenLogRepository repository;

    @Transactional
    public void revogarPorRefreshTokenId(String refreshTokenId) {
        var optionalLog = repository.findByRefreshTokenId(refreshTokenId);
        optionalLog.ifPresent(log -> {
            log.setRevoked(true);
            repository.save(log);
        });
    }
}