package br.uece.alunos.sisreserva.v1.dto.usuario;

import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;

public sealed interface LoginInternoResultDTO {
    record Autenticado(AuthTokensDTO tokens) implements LoginInternoResultDTO {}
    record OnboardingRequerido(String onboardingToken) implements LoginInternoResultDTO {}
}