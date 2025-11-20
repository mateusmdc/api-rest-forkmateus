package br.uece.alunos.sisreserva.v1.dto.secretariaEspaco;

import jakarta.validation.constraints.NotEmpty;

/**
 * DTO para receber os dados de criação de uma secretaria de espaço.
 * Representa a entrada de dados para vincular um usuário à secretaria de um espaço.
 */
public record SecretariaEspacoDTO(
        /**
         * ID do usuário que fará parte da secretaria
         */
        @NotEmpty(message = "O campo usuarioSecretariaId não pode ser vazio")
        String usuarioSecretariaId,

        /**
         * ID do espaço ao qual o usuário será vinculado
         */
        @NotEmpty(message = "O campo espacoId não pode ser vazio")
        String espacoId
) {}
