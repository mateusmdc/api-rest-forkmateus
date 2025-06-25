package br.uece.alunos.sisreserva.v1.domain.usuario.DTO;

import java.time.LocalDateTime;

public record UsuarioEsqueciSenhaDTO(String tokenMail,
                                     LocalDateTime tokenExpiration) {
}
