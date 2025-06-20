package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntUsuarioPorId {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario obterEntidade(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado usuário (entidade) com o ID informado."));
    }

}
