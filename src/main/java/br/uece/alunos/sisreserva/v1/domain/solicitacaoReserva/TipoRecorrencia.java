package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva;

/**
 * Enumeração que define os tipos de recorrência para solicitações de reserva.
 * 
 * <p>Permite que uma reserva seja agendada de forma única ou recorrente,
 * facilitando o agendamento em massa de horários repetitivos.</p>
 * 
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
public enum TipoRecorrencia {
    /**
     * Reserva única, sem repetição.
     * A reserva ocorre apenas na data e horário especificados.
     */
    NAO_REPETE(0, "Não se repete"),
    
    /**
     * Reserva com recorrência diária.
     * A reserva se repete todos os dias no mesmo horário.
     */
    DIARIA(1, "Diária"),
    
    /**
     * Reserva com recorrência semanal.
     * A reserva se repete semanalmente no mesmo dia da semana e horário.
     */
    SEMANAL(2, "Semanal"),
    
    /**
     * Reserva com recorrência mensal.
     * A reserva se repete mensalmente no mesmo dia do mês e horário.
     */
    MENSAL(3, "Mensal");

    private final int codigo;
    private final String descricao;

    /**
     * Construtor privado do enum.
     * 
     * @param codigo código numérico do tipo de recorrência
     * @param descricao descrição legível do tipo de recorrência
     */
    TipoRecorrencia(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /**
     * Retorna o código numérico do tipo de recorrência.
     * 
     * @return código numérico
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Retorna a descrição legível do tipo de recorrência.
     * 
     * @return descrição do tipo
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte um código numérico para o tipo de recorrência correspondente.
     * 
     * @param codigo código numérico a ser convertido
     * @return TipoRecorrencia correspondente ao código
     * @throws IllegalArgumentException se o código for inválido
     */
    public static TipoRecorrencia fromCodigo(int codigo) {
        for (TipoRecorrencia tipo : values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido para TipoRecorrencia: " + codigo);
    }
}
