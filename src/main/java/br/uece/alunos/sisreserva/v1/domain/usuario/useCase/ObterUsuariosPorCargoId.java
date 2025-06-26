package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ObterUsuariosPorCargoId {
    @Autowired
    private UsuarioRepository repository;

    public Page<UsuarioRetornoDTO> obterUsuariosPorCargo(String cargoId, Pageable pageable) {
        return repository.findAllUsuariosByCargoId(cargoId, pageable).map(UsuarioRetornoDTO::new);
    }
}
