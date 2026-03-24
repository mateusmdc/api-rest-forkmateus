package br.uece.alunos.sisreserva.v1.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record OnboardingUsuarioInternoDTO(
        @NotEmpty(message = "O token de onboarding é obrigatório")
        String onboardingToken,

        @NotEmpty(message = "O nome é obrigatório")
        String nome,

        @NotEmpty(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        String documentoFiscal,

        String telefone,

        @NotEmpty(message = "O ID da instituição é obrigatório")
        String instituicaoId
) {}