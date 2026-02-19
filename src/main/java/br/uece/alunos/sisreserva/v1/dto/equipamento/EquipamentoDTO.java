package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de equipamento.
 * 
 * @param tombamento número de tombamento do equipamento
 * @param descricao descrição do equipamento
 * @param statusEquipamento status do equipamento (obrigatório)
 * @param tipoEquipamentoId identificador do tipo de equipamento (obrigatório)
 * @param multiusuario indica se o equipamento pode ser usado por múltiplos usuários (default: false)
 * @param reservavel indica se o equipamento está disponível para reserva (default: true)
 * @param espacoId identificador do espaço ao qual o equipamento será vinculado (opcional)
 */
public record EquipamentoDTO(
        String tombamento,
        String descricao,
        @NotNull(message = "O status do equipamento é obrigatório")
        StatusEquipamento statusEquipamento,
        @NotBlank(message = "O ID do tipo de equipamento é obrigatório")
        String tipoEquipamentoId,
        Boolean multiusuario,
        Boolean reservavel,
        String espacoId
) {}
