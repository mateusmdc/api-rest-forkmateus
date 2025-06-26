package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargoRepository;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.UsuarioCargoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObterCargosUsuarioId {
    @Autowired
    private UsuarioCargoRepository usuarioCargoRepository;

    public List<UsuarioCargoRetornoDTO> obterCargosPorIdUsuario(String idUsuario) {
        return usuarioCargoRepository.findByUsuarioIdComCargo(idUsuario)
                .stream()
                .map(UsuarioCargoRetornoDTO::new)
                .toList();
    }
}
