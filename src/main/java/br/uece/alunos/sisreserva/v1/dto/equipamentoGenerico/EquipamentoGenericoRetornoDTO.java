package br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;

import java.time.LocalDateTime;

/**
 * DTO de retorno para equipamentos genéricos.
 * Contém todos os dados do equipamento incluindo timestamps de auditoria.
 * 
 * @param id Identificador único do equipamento genérico
 * @param nome Nome do equipamento genérico
 * @param createdAt Data e hora de criação
 * @param updatedAt Data e hora da última atualização
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public record EquipamentoGenericoRetornoDTO(
        String id,
        String nome,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Construtor que converte uma entidade EquipamentoGenerico para DTO.
     * 
     * @param equipamento Entidade a ser convertida
     */
    public EquipamentoGenericoRetornoDTO(EquipamentoGenerico equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getCreatedAt(),
                equipamento.getUpdatedAt()
        );
    }
}
