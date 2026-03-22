package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.util.List;

/**
 * DTO de retorno com informações completas sobre uma série de reservas recorrentes.
 *
 * No novo modelo, apenas um registro é armazenado no banco para cada série
 * recorrente. As ocorrências individuais são calculadas dinamicamente a partir das regras
 * de recorrência, com as exceções registradas aplicadas sobre elas.
 *
 * @param serie            dados da série de reservas (o registro armazenado no banco)
 * @param ocorrencias      lista de ocorrências calculadas dinamicamente, com exceções aplicadas
 * @param totalOcorrencias número total de ocorrências na série
 */
public record RecorrenciaInfoDTO(
        SolicitacaoReservaRetornoDTO serie,
        List<OcorrenciaReservaDTO> ocorrencias,
        Integer totalOcorrencias
) {
    /**
     * Construtor auxiliar que calcula {@code totalOcorrencias} automaticamente.
     *
     * @param serie       dados da série
     * @param ocorrencias lista de ocorrências calculadas
     */
    public RecorrenciaInfoDTO(SolicitacaoReservaRetornoDTO serie, List<OcorrenciaReservaDTO> ocorrencias) {
        this(serie, ocorrencias, ocorrencias != null ? ocorrencias.size() : 0);
    }
}
