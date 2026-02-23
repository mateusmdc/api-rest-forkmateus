package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Returns a UsuarioRetornoDTO for a given user ID.
 */
@Component
@AllArgsConstructor
public class ObterUsuarioPorId {

    private final ObterEntUsuarioPorId obterEntUsuarioPorId;

    public UsuarioRetornoDTO obter(String id) {
        var usuario = obterEntUsuarioPorId.obterEntidade(id);
        return new UsuarioRetornoDTO(usuario);
    }
}