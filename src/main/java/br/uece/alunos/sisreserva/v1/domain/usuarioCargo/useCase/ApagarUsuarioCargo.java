package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargoRepository;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApagarUsuarioCargo {
    @Autowired
    private UsuarioCargoRepository usuarioCargoRepository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    public void remover(ApagarUsuarioCargoDTO data) {
        try {
            var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioId());
            var cargo = entityHandlerService.obterCargoPorId(data.cargoId());

            UsuarioCargo usuarioCargo = usuarioCargoRepository
                    .findByUsuarioIdAndCargoId(usuario.getId(), cargo.getId())
                    .orElseThrow(() -> new ValidationException("O vínculo entre usuário e cargo não existe."));

            usuarioCargoRepository.delete(usuarioCargo);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
