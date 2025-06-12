package br.uece.alunos.api_aluga_espacos.v1.infra.security;

public record AuthTokensDTO(String accessToken, String refreshToken) {
}
