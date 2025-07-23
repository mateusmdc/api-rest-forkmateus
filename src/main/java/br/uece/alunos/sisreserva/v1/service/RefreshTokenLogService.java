package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

public interface RefreshTokenLogService {
    void registrar(Usuario usuario, String refreshTokenId, LocalDateTime issuedAt, LocalDateTime expiresAt, HttpServletRequest request);
    void revogarPorRefreshTokenId(String refreshTokenId);
}
