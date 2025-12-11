package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.*;
import java.util.List;

public record UsuarioDTO(
        @NotEmpty(message = "O nome é obrigatório")
        String nome,

        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "A senha deve conter pelo menos uma letra maiúscula e um número")
        @NotEmpty(message = "A senha é obrigatória")
        String senha,

        @NotEmpty(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotEmpty
        String documentoFiscal,

        String fotoPerfil,

        String matricula,

        String telefone,

        @NotEmpty(message = "O ID da instituição é obrigatório")
        String instituicaoId,

        boolean refreshTokenEnabled,

        @NotEmpty(message = "Pelo menos um cargo deve ser informado")
        List<@NotEmpty(message = "Lista de cargos não pode estar em branco") String> cargosNome

) {}
