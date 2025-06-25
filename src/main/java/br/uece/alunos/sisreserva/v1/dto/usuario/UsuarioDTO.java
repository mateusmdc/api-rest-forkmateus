package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.*;
import java.util.List;

public record UsuarioDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "A senha deve conter pelo menos uma letra maiúscula e um número")
        @NotNull
        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        String fotoPerfil,

        @Positive(message = "A matrícula deve ser um número positivo")
        int matricula,

        String telefone,

        @NotBlank(message = "O ID da instituição é obrigatório")
        String instituicaoId,

        boolean refreshTokenEnabled,

        @NotEmpty(message = "Pelo menos um cargo deve ser informado")
        List<@NotBlank(message = "Lista de cargos não pode estar em branco") String> cargosNome

) {}
