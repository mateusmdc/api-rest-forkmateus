package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para gerenciamento de exceções de ocorrências em séries recorrentes.
 *
 * Uma exceção é criada quando uma ocorrência específica de uma série precisa
 * ter status, horário ou motivo diferente do padrão da série.
 *
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
public interface ExcecaoRecorrenciaRepository extends JpaRepository<ExcecaoRecorrencia, String> {

    /**
     * Busca todas as exceções de uma série de reservas recorrentes, ordenadas por data.
     *
     * @param serieId ID da série ({@code SolicitacaoReserva.id} com {@code isSerie = true})
     * @return lista de exceções da série, ordenada por data da ocorrência
     */
    @Query("SELECT e FROM ExcecaoRecorrencia e WHERE e.solicitacaoReservaId = :serieId ORDER BY e.dataOcorrencia ASC")
    List<ExcecaoRecorrencia> findBySerieId(@Param("serieId") String serieId);

    /**
     * Busca a exceção de uma série para uma data específica de ocorrência.
     *
     * @param serieId        ID da série
     * @param dataOcorrencia data da ocorrência alvo
     * @return exceção registrada para aquela data, se existir
     */
    @Query("SELECT e FROM ExcecaoRecorrencia e WHERE e.solicitacaoReservaId = :serieId AND e.dataOcorrencia = :data")
    Optional<ExcecaoRecorrencia> findBySerieIdAndData(
            @Param("serieId") String serieId,
            @Param("data") LocalDate dataOcorrencia
    );

    /**
     * Busca exceções de múltiplas séries em uma única consulta (carregamento em lote).
     * Útil para evitar N+1 queries ao carregar exceções de várias séries de uma vez.
     *
     * @param serieIds lista de IDs de séries
     * @return lista de todas as exceções das séries informadas
     */
    @Query("SELECT e FROM ExcecaoRecorrencia e WHERE e.solicitacaoReservaId IN :serieIds ORDER BY e.dataOcorrencia ASC")
    List<ExcecaoRecorrencia> findBySerieIds(@Param("serieIds") List<String> serieIds);
}
