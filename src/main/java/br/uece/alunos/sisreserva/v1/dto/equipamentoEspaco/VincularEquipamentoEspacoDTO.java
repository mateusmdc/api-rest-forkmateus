package br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para vincular um equipamento existente a um espaço existente.
 * 
 * @param equipamentoId ID do equipamento a ser vinculado
 * @param espacoId ID do espaço ao qual o equipamento será vinculado
 * @param usuarioId ID do usuário que está fazendo a vinculação (deve ser gestor do espaço)
 */
public record VincularEquipamentoEspacoDTO(
    @NotBlank(message = "O ID do equipamento é obrigatório")
    String equipamentoId,
    
    @NotBlank(message = "O ID do espaço é obrigatório")
    String espacoId,
    
    @NotBlank(message = "O ID do usuário é obrigatório")
    String usuarioId
) {
}
