package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.useCase.RegistrarLogRefreshToken;
import br.uece.alunos.sisreserva.v1.domain.refreshTokenLog.useCase.RevogarRefreshToken;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.service.RefreshTokenLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenLogServiceImpl implements RefreshTokenLogService {
    private final RegistrarLogRefreshToken registrarLogRefreshToken;
    private final RevogarRefreshToken revogarRefreshToken;

    @Override
    public void registrar(Usuario usuario, String refreshTokenId, LocalDateTime issuedAt, LocalDateTime expiresAt, HttpServletRequest request) {
        registrarLogRefreshToken.registrar(usuario, refreshTokenId, issuedAt, expiresAt, request);
    }

    @Override
    public void revogarPorRefreshTokenId(String refreshTokenId) {
        revogarRefreshToken.revogarPorRefreshTokenId(refreshTokenId);
    }
}
