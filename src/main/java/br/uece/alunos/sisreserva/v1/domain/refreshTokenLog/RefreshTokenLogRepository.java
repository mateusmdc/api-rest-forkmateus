package br.uece.alunos.sisreserva.v1.domain.refreshTokenLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenLogRepository extends JpaRepository<RefreshTokenLog, String> {

    Optional<RefreshTokenLog> findByRefreshTokenId(String refreshTokenId);
}
