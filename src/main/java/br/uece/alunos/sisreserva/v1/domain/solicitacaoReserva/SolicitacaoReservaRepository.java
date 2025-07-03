package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SolicitacaoReservaRepository extends JpaRepository<SolicitacaoReserva, String> {
    @Query("SELECT sr FROM SolicitacaoReserva sr WHERE sr.usuarioSolicitante.id = :usuarioId")
    List<SolicitacaoReserva> findByUsuarioSolicitanteId(String usuarioId);

    @Query("SELECT sr FROM SolicitacaoReserva sr WHERE sr.espaco.id = :espacoId")
    List<SolicitacaoReserva> findByEspacoId(String espacoId);

    @Query("SELECT sr FROM SolicitacaoReserva sr WHERE sr.status = :status")
    List<SolicitacaoReserva> findByStatus(StatusSolicitacao status);

    @Query("SELECT sr FROM SolicitacaoReserva sr WHERE sr.projeto.id = :projetoId")
    List<SolicitacaoReserva> findByProjetoId(String projetoId);

    @Query("SELECT sr FROM SolicitacaoReserva sr WHERE sr.dataInicio >= :startDate AND sr.dataFim <= :endDate")
    List<SolicitacaoReserva> findByPeriodo(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT sr FROM SolicitacaoReserva sr WHERE sr.status = :status AND sr.espaco.id = :espacoId")
    List<SolicitacaoReserva> findByStatusAndEspacoId(StatusSolicitacao status, String espacoId);

    @Query("SELECT sr FROM SolicitacaoReserva sr ORDER BY sr.createdAt DESC")
    Page<SolicitacaoReserva> findAllPageable(Pageable pageable);
}
