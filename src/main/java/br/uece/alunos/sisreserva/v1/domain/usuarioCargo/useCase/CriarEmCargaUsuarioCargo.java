package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.UsuarioCargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargoRepository;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriarEmCargaUsuarioCargo {
    @Autowired
    private UsuarioCargoRepository usuarioCargoRepository;
    @Autowired
    private EntityHandlerService entityHandlerService;

    public List<UsuarioCargoRetornoDTO> criarEmCargaUsuarioCargo(CriarCargaUsuarioCargoDTO data) {
        var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioId());

        return null;
    }
}
