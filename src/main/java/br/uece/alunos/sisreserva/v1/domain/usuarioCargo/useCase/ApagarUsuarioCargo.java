package br.uece.alunos.sisreserva.v1.domain.usuarioCargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargo;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.UsuarioCargoRepository;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.*;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApagarUsuarioCargo {
    @Autowired
    private UsuarioCargoRepository usuarioCargoRepository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Transactional
    public void remover(ApagarUsuarioCargoDTO data) {
        try {
            var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioId());
            var cargo = entityHandlerService.obterCargoPorId(data.cargoId());

            UsuarioCargo usuarioCargo = usuarioCargoRepository
                    .findByUsuarioIdAndCargoId(usuario.getId(), cargo.getId())
                    .orElseThrow(() -> new ValidationException("O vínculo entre usuário e cargo não existe."));

            // Com orphanRemoval = true, ao remover a entidade da coleção usuarioCargos do usuário,
            // o Hibernate automaticamente identifica que o objeto ficou órfão e gera um DELETE no banco
            // para remover o registro correspondente da tabela usuario_cargo ao executar o flush ou commit.
            usuario.getUsuarioCargos().remove(usuarioCargo);
            usuarioCargoRepository.flush();

        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }
}