package br.uece.alunos.sisreserva.v1.dto.espaco;

/**
 * DTO para totais agregados de um período.
 * 
 * <p>Contém informações totalizadas de reservas em um período específico.</p>
 * 
 * @param totalReservasSolicitadas quantidade total de reservas solicitadas no período (todos os status)
 * @param totalReservasAprovadas quantidade total de reservas aprovadas no período (status = APROVADO)
 */
public record TotaisPeriodoDTO(
    Long totalReservasSolicitadas,
    Long totalReservasAprovadas
) {}
