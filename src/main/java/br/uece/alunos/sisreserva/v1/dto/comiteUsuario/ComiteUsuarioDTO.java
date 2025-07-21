package br.uece.alunos.sisreserva.v1.dto.comiteUsuario;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComiteUsuarioDTO(
        @NotEmpty(message = "comiteId não pode ser vazio")
        String comiteId,
        @NotEmpty(message = "usuarioId não pode ser vazio")
        String usuarioId,
        String departamentoId,
        @Size(max = 100, message = "descricao pode ter no máximo 100 caracteres")
        String descricao,
        @NotEmpty(message = "portaria não pode ser vazia")
        @Size(max = 50, message = "portaria pode ter no máximo 50 caracteres")
        String portaria,
        @NotNull(message = "isTitular não pode ser nulo")
        Boolean isTitular
) {}
