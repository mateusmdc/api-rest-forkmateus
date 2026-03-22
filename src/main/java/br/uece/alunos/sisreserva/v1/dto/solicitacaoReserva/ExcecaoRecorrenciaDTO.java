package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de entrada para criação ou atualização de uma exceção em uma ocorrência
 * específica de uma série de reservas recorrentes.
 *
 * Permite que um gestor ou administrador:
 * 
 *   - Cancele apenas uma ocorrência da série (sem afetar as demais).
 *   - Altere status (ex.: aprovar ou recusar) de uma ocorrência individualmente.
 *   - Reagende uma ocorrência para outro horário.
 * 
 *
 * @param dataOcorrencia data da ocorrência específica a ser modificada (obrigatório)
 * @param novoStatus     novo status para esta ocorrência (obrigatório)
 * @param dataInicioNova novo horário de início; se {@code null}, mantém o horário padrão da série
 * @param dataFimNova    novo horário de fim; se {@code null}, mantém a duração padrão da série
 * @param motivo         motivo da exceção (opcional)
 */
public record ExcecaoRecorrenciaDTO(
        @NotNull(message = "A data da ocorrência é obrigatória")
        LocalDate dataOcorrencia,

        @NotNull(message = "O novo status é obrigatório")
        StatusSolicitacao novoStatus,

        LocalDateTime dataInicioNova,
        LocalDateTime dataFimNova,
        String motivo
) {
}
