package br.uece.alunos.sisreserva.v1.dto.espaco;

import java.util.List;

/**
 * DTO para atualização parcial de espaço.
 * Todos os campos são opcionais.
 * 
 * @param nome nome do espaço
 * @param urlCnpq URL do CNPq
 * @param observacao observações sobre o espaço
 * @param precisaProjeto indica se o espaço precisa de projeto vinculado
 * @param multiusuario indica se o espaço pode ser usado por diferentes tipos de usuários
 * @param reservavel indica se o espaço está disponível para reserva
 * @param tipoAtividadeIds lista de IDs dos tipos de atividade (opcional)
 */
public record EspacoAtualizarDTO(
        String nome, 
        String urlCnpq, 
        String observacao, 
        Boolean precisaProjeto,
        Boolean multiusuario,
        Boolean reservavel,
        List<String> tipoAtividadeIds
) {
}
