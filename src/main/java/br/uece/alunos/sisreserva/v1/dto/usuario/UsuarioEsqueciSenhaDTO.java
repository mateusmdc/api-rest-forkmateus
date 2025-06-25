package br.uece.alunos.sisreserva.v1.dto.usuario;

import java.time.LocalDateTime;

public record UsuarioEsqueciSenhaDTO(String tokenMail,
                                     LocalDateTime tokenExpiration) {
}
