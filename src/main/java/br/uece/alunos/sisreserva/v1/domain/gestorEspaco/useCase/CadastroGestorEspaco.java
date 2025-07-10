package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation.GestorEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CadastroGestorEspaco {
    @Autowired
    private GestorEspacoRepository repository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private GestorEspacoValidator validator;

    public GestorEspacoRetornoDTO cadastrarGestorEspaco(GestorEspacoDTO data) {
        validator.validarSeUsuarioJaEGestorDoEspaco(data.usuarioGestorId(), data.espacoId());

        var usuarioGestor = entityHandlerService.obterUsuarioPorId(data.usuarioGestorId());
        var espaco = entityHandlerService.obterEspacoPorId(data.espacoId());

        var gestorEspaco = new GestorEspaco(usuarioGestor, espaco);

        var gestorEspacoSalvo = repository.save(gestorEspaco);

        return new GestorEspacoRetornoDTO(gestorEspacoSalvo);
    }
}
