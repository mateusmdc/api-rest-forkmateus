package br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuario;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.validation.ComiteUsuarioValidator;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@AllArgsConstructor
public class CriarComiteUsuario {
    private final ComiteUsuarioRepository repository;
    private final EntityHandlerService entityHandlerService;
    private final ComiteUsuarioValidator validator;

    @Transactional
    public ComiteUsuarioRetornoDTO criar(ComiteUsuarioDTO data) {
        var usuario = entityHandlerService.obterUsuarioPorId(data.usuarioId());
        var comite = entityHandlerService.obterComitePorId(data.comiteId());

        validator.validarUsuarioParaComite(usuario, comite);

        var departamento = obterDepartamentoSeInformado(data);

        var comiteUsuario = new ComiteUsuario(data, comite, usuario, departamento);
        var salvo = repository.save(comiteUsuario);

        return new ComiteUsuarioRetornoDTO(salvo);
    }

    private Departamento obterDepartamentoSeInformado(ComiteUsuarioDTO data) {
        if (!StringUtils.hasText(data.departamentoId())) return null;
        return entityHandlerService.obterDepartamentoPorId(data.departamentoId());
    }
}