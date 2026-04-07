package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.util.List;

/**
 * DTO que representa uma série recorrente e suas ocorrências aprovadas em um determinado mês,
 * retornado na listagem de horários ocupados de um espaço ou equipamento.
 *
 * Os campos {@code espacoId}/{@code espacoNome} estão preenchidos quando a série é de um
 * espaço; {@code equipamentoId}/{@code equipamentoNome} estão preenchidos quando a série é de
 * um equipamento. Apenas um par é populado por vez.
 *
 * @param serieId            ID da série ({@code SolicitacaoReserva.id} com {@code isSerie = true})
 * @param espacoId           ID do espaço reservado, ou {@code null} para séries de equipamento
 * @param espacoNome         nome do espaço reservado, ou {@code null} para séries de equipamento
 * @param equipamentoId      ID do equipamento reservado, ou {@code null} para séries de espaço
 * @param equipamentoNome    nome/tombamento do equipamento reservado, ou {@code null} para séries de espaço
 * @param usuarioSolicitante nome do usuário que criou a série
 * @param projetoNome        nome do projeto associado, ou {@code null} se não houver
 * @param ocorrenciasNoMes   lista de ocorrências aprovadas da série que caem no mês filtrado
 */
public record SerieHorariosOcupadosDTO(
    String serieId,
    String espacoId,
    String espacoNome,
    String equipamentoId,
    String equipamentoNome,
    String usuarioSolicitante,
    String projetoNome,
    List<HorarioOcupadoDTO> ocorrenciasNoMes
) {}
