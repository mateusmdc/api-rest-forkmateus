package br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.validation.ComiteUsuarioValidator;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.departamento.DepartamentoRepository;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class AtualizarComiteUsuario {
    private final ComiteUsuarioRepository repository;
    private final ComiteUsuarioValidator validator;
    private final EntityHandlerService entityHandlerService;

    @Transactional
    public ComiteUsuarioRetornoDTO atualizar(String id, ComiteUsuarioAtualizarDTO data) {
        validator.validarComiteUsuarioId(id);

        var comiteUsuario = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuário Comitê não encontrado, mesmo após a validação do ID."));

        Departamento departamento = null;
        if (data.departamentoId() != null && !data.departamentoId().trim().isEmpty()) {
            departamento = entityHandlerService.obterDepartamentoPorId(data.departamentoId());
        }

        comiteUsuario.atualizar(data, departamento);

        var salvoNoBanco = repository.save(comiteUsuario);

        return new ComiteUsuarioRetornoDTO(salvoNoBanco);
    }
}
