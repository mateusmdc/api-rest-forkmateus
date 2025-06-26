package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargoRepository;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarUsuarioCargo {
    @Autowired
    private UsuarioCargoRepository usuarioCargoRepository;
    @Autowired
    private EntityHandlerService entityHandlerService;

    public UsuarioCargoRetornoDTO criar(CriarUsuarioCargoDTO data) {
        try {
            var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioId());
            var cargo = entityHandlerService.obterCargoPorId(data.cargoId());

            if (!usuario.isEnabled()) {
                throw new ValidationException("O usuário está inativo e não pode receber cargos.");
            }

            boolean jaExiste = usuarioCargoRepository.existsByUsuarioIdAndCargoId(usuario.getId(), cargo.getId());
            if (jaExiste) {
                throw new ValidationException("O cargo informado já está atribuído ao usuário.");
            }

            var novo = new UsuarioCargo(usuario, cargo);
            var salvo = usuarioCargoRepository.save(novo);

            return new UsuarioCargoRetornoDTO(salvo);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

}
