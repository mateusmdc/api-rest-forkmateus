package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;

/**
 * DTO para retorno de informações de equipamento.
 * 
 * @param id identificador único do equipamento
 * @param tombamento número de tombamento
 * @param descricao descrição do equipamento
 * @param status código do status do equipamento
 * @param tipoEquipamento dados do tipo de equipamento
 * @param multiusuario indica se o equipamento é multiusuário
 * @param reservavel indica se o equipamento está disponível para reserva
 */
public record EquipamentoRetornoDTO(
        String id,
        String tombamento,
        String descricao,
        int status,
        TipoEquipamentoRetornoDTO tipoEquipamento,
        Boolean multiusuario,
        Boolean reservavel
) {
    /**
     * Construtor que converte uma entidade Equipamento para o DTO de retorno.
     * 
     * @param e entidade Equipamento a ser convertida
     */
    public EquipamentoRetornoDTO(Equipamento e) {
        this(
                e.getId(),
                e.getTombamento(),
                e.getDescricao(),
                e.getStatus().getCodigo(),
                new TipoEquipamentoRetornoDTO(e.getTipoEquipamento()),
                e.getMultiusuario(),
                e.getReservavel()
        );
    }
}
