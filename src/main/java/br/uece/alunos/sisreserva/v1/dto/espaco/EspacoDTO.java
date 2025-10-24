package br.uece.alunos.sisreserva.v1.dto.espaco;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de espaço.
 * 
 * @param nome nome do espaço
 * @param urlCnpq URL do CNPq (opcional)
 * @param observacao observações sobre o espaço (opcional)
 * @param departamentoId identificador do departamento
 * @param localizacaoId identificador da localização
 * @param tipoEspacoId identificador do tipo de espaço
 * @param tipoAtividadeId identificador do tipo de atividade
 * @param precisaProjeto indica se o espaço precisa de projeto vinculado
 * @param multiusuario indica se o espaço pode ser usado por diferentes tipos de usuários (default: false)
 */
public record EspacoDTO(
        @NotEmpty(message = "O nome não pode ser vazio")
        String nome,
        String urlCnpq,
        String observacao,
        @NotEmpty(message = "O departamentoId não pode ser vazio")
        String departamentoId,
        @NotEmpty(message = "O localizacaoId não pode ser vazio")
        String localizacaoId,
        @NotEmpty(message = "O tipoEspacoId não pode ser vazio")
        String tipoEspacoId,
        @NotEmpty(message = "O tipoAtividadeId não pode ser vazio")
        String tipoAtividadeId,
        @NotNull(message = "O campo precisaProjeto é obrigatório")
        Boolean precisaProjeto,
        Boolean multiusuario // Opcional - default: false
) {}
