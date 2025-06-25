package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ObterUsuariosPaginado {
    @Autowired
    private UsuarioRepository repository;

    public Page<UsuarioRetornoDTO> obterUsuarios(Pageable pageable) {
        return repository.findAllUsuariosPageable(pageable).map(UsuarioRetornoDTO::new);
    }
}
