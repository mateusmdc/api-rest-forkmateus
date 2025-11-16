package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.util.List;

/**
 * DTO para retorno de informações sobre reservas recorrentes.
 * 
 * <p>Contém a reserva pai e todas as reservas filhas geradas pela recorrência.</p>
 * 
 * @param reservaPai reserva principal (primeira ocorrência)
 * @param reservasFilhas lista de reservas geradas pela recorrência
 * @param totalOcorrencias número total de ocorrências (incluindo a reserva pai)
 */
public record RecorrenciaInfoDTO(
    SolicitacaoReservaRetornoDTO reservaPai,
    List<SolicitacaoReservaRetornoDTO> reservasFilhas,
    Integer totalOcorrencias
) {
    /**
     * Construtor auxiliar para criar a partir de uma lista completa de reservas.
     * 
     * @param reservaPai reserva principal
     * @param reservasFilhas lista de reservas filhas
     */
    public RecorrenciaInfoDTO(
            SolicitacaoReservaRetornoDTO reservaPai, 
            List<SolicitacaoReservaRetornoDTO> reservasFilhas) {
        this(reservaPai, reservasFilhas, 1 + (reservasFilhas != null ? reservasFilhas.size() : 0));
    }
}
