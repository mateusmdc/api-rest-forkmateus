package br.uece.alunos.sisreserva.v1.dto.usuario;

import java.util.List;

public record AtualizarUsuarioDTO(String nome, String fotoPerfil, Integer matricula, String telefone, String instituicaoId,
                                  Boolean refreshTokenEnabled, List<String> cargosId) {
}
