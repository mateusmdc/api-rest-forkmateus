package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.time.LocalDateTime;

/**
 * DTO que representa um horário ocupado por uma reserva aprovada.
 *
 * Os campos {@code espacoId}/{@code espacoNome} estão preenchidos quando a reserva é
 * de um espaço; {@code equipamentoId}/{@code equipamentoNome} estão preenchidos quando
 * a reserva é de um equipamento. Apenas um par é populado por vez.
 *
 * @param espacoId           ID do espaço reservado, ou {@code null} para reservas de equipamento
 * @param espacoNome         nome do espaço reservado, ou {@code null} para reservas de equipamento
 * @param equipamentoId      ID do equipamento reservado, ou {@code null} para reservas de espaço
 * @param equipamentoNome    nome/tombamento do equipamento reservado, ou {@code null} para reservas de espaço
 * @param dataInicio         data e hora de início da ocupação
 * @param dataFim            data e hora de fim da ocupação
 * @param usuarioSolicitante nome do usuário que realizou a reserva
 * @param projetoNome        nome do projeto associado, ou {@code null} se não houver
 */
public record HorarioOcupadoDTO(
    String espacoId,
    String espacoNome,
    String equipamentoId,
    String equipamentoNome,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    String usuarioSolicitante,
    String projetoNome
) {}