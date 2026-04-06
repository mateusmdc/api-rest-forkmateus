package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO que representa uma ocorrência individual calculada de uma série de reservas recorrentes.
 *
 * Este DTO não é armazenado no banco de dados. É gerado dinamicamente
 * pelo sistema a partir das regras de recorrência da série e das exceções registradas em
 * {@code excecao_recorrencia}. Cada instância representa uma "data de reserva" dentro da série.
 *
 * Se a ocorrência não possui exceção, ela herda status, horário de início e duração da série.
 * Caso exista uma exceção, os campos efetivos refletem os valores sobrescritos.
 *
 * @param serieId        ID da série ({@code SolicitacaoReserva.id} com {@code isSerie = true})
 * @param dataOcorrencia data original da ocorrência conforme calculada pelo algoritmo de recorrência
 * @param dataInicio     horário efetivo de início (da série ou da exceção)
 * @param dataFim        horário efetivo de fim (da série ou da exceção)
 * @param status         código de status efetivo (da série ou da exceção)
 * @param temExcecao     {@code true} se existe registro em {@code excecao_recorrencia} para esta data
 * @param excecaoId      ID do registro de exceção, ou {@code null} se não houver
 * @param motivo         motivo registrado na exceção, ou {@code null} se não houver
 */
public record OcorrenciaReservaDTO(
        String serieId,
        LocalDate dataOcorrencia,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer status,
        boolean temExcecao,
        String excecaoId,
        String motivo
) {
}
