package br.uece.alunos.sisreserva.v1.dto.espaco;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
        Boolean precisaProjeto
) {}
