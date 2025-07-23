package br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.useCase;

import br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.RefreshTokenLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaSeFoiRevogado {
    @Autowired
    private RefreshTokenLogRepository repository;

    public boolean foiRevogado(String refreshTokenId) {
        return repository.existsByIdAndRevokedTrue(refreshTokenId);
    }
}
