package br.uece.alunos.sisreserva.v1.dto.espaco;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO para criação de espaço.
 * 
 * @param nome nome do espaço
 * @param urlCnpq URL do CNPq (opcional)
 * @param observacao observações sobre o espaço (opcional)
 * @param departamentoId identificador do departamento
 * @param localizacaoId identificador da localização
 * @param tipoEspacoId identificador do tipo de espaço
 * @param tipoAtividadeIds lista de identificadores dos tipos de atividade (mínimo 1)
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
        @NotEmpty(message = "Deve haver pelo menos um tipo de atividade")
        List<String> tipoAtividadeIds,
        @NotNull(message = "O campo precisaProjeto é obrigatório")
        Boolean precisaProjeto,
        Boolean multiusuario // Opcional - default: false
) {}
