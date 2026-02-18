package br.uece.alunos.sisreserva.v1.dto.equipamento;

import br.uece.alunos.sisreserva.v1.domain.equipamento.StatusEquipamento;

/**
 * DTO para atualização parcial de equipamento.
 * Todos os campos são opcionais.
 * 
 * @param usuarioId identificador do usuário
 * @param descricao descrição do equipamento
 * @param status status do equipamento
 * @param multiusuario indica se o equipamento pode ser usado por múltiplos usuários
 * @param reservavel indica se o equipamento está disponível para reserva
 */
public record EquipamentoAtualizarDTO(
        String usuarioId, 
        String descricao, 
        StatusEquipamento status, 
        Boolean multiusuario,
        Boolean reservavel
) {
}
