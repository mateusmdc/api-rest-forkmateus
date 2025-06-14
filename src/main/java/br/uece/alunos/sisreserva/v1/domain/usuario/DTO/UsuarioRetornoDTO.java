package br.uece.alunos.sisreserva.v1.domain.usuario.DTO;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public record UsuarioRetornoDTO(String nome, String email, String fotoPerfil,
                                int matricula, String telefone, String instituicaoId, String instituicaoNome,
                                boolean refreshTokenEnabled, List<String> cargosId, List<String> cargosNome) {
    public UsuarioRetornoDTO(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getFotoPerfil(), usuario.getMatricula(), usuario.getTelefone(),
                usuario.getInstituicao().getId(), usuario.getInstituicao().getNome(), usuario.isRefreshTokenEnabled(),
                new ArrayList<>(), new ArrayList<>());
    }
}
