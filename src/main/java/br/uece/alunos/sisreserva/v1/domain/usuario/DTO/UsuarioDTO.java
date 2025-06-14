package br.uece.alunos.sisreserva.v1.domain.usuario.DTO;

import java.util.List;

public record UsuarioDTO(String nome, String senha, String email, String fotoPerfil,
                         int matricula, String telefone, String instituicaoId, boolean refreshTokenEnabled, List<String> cargosId) {
}
