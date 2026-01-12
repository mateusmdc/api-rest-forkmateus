package br.uece.alunos.sisreserva.v1.dto.espaco;

/**
 * Projeção para contagem de reservas agrupadas por mês/ano.
 * 
 * <p>Interface usada para receber resultados de queries agregadas
 * que contam reservas por período.</p>
 */
public interface ReservasPorMesProjection {
    Integer getMes();
    Integer getAno();
    Long getTotalReservas();
    Long getReservasConfirmadas();
}
