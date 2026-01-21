package br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de solicitação de reserva.
 * 
 * <p>Suporta tanto reservas únicas quanto reservas recorrentes.
 * Para reservas recorrentes, é necessário especificar o tipoRecorrencia
 * e opcionalmente a dataFimRecorrencia.</p>
 * 
 * <p><strong>IMPORTANTE:</strong> Uma solicitação pode ser de espaço OU equipamento, nunca ambos.
 * Exatamente um dos campos (espacoId ou equipamentoId) deve estar preenchido.</p>
 * 
 * @param dataInicio data e hora de início da reserva
 * @param dataFim data e hora de fim da reserva
 * @param espacoId identificador do espaço a ser reservado (exclusivo com equipamentoId)
 * @param equipamentoId identificador do equipamento a ser reservado (exclusivo com espacoId)
 * @param usuarioSolicitanteId identificador do usuário solicitante
 * @param projetoId identificador do projeto (opcional)
 * @param tipoRecorrencia tipo de recorrência da reserva (0=Não repete, 1=Diária, 2=Semanal, 3=Mensal)
 * @param dataFimRecorrencia data até quando a recorrência deve se repetir (opcional, obrigatório se tipoRecorrencia != 0)
 */
public record SolicitacaoReservaDTO(
    @NotNull(message = "Data de início é obrigatória")
    LocalDateTime dataInicio,

    @NotNull(message = "Data de fim é obrigatória")
    LocalDateTime dataFim,

    String espacoId, // Exclusivo com equipamentoId

    String equipamentoId, // Exclusivo com espacoId

    @NotNull(message = "Usuário solicitante é obrigatório")
    String usuarioSolicitanteId,

    String projetoId, // Optional
    
    Integer tipoRecorrencia, // Optional - Default 0 (NAO_REPETE)
    
    LocalDateTime dataFimRecorrencia // Optional - Required if tipoRecorrencia != 0
) {}