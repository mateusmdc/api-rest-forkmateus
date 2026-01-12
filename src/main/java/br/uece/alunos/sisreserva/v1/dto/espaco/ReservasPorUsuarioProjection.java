package br.uece.alunos.sisreserva.v1.dto.espaco;

/**
 * Projeção para contagem de reservas agrupadas por usuário.
 * 
 * <p>Interface usada para receber resultados de queries agregadas
 * que contam reservas por usuário.</p>
 */
public interface ReservasPorUsuarioProjection {
    String getUsuarioId();
    String getUsuarioNome();
    Long getTotalReservas();
    Long getReservasConfirmadas();
}
