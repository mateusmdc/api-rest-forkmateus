package br.uece.alunos.sisreserva.v1.infra.security;

public record AuthTokensDTO(String accessToken, String refreshToken) {
}
