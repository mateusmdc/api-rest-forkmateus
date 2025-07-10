package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation.GestorEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CadastraOuReativaGestorEspaco {
    @Autowired
    private GestorEspacoRepository repository;

    @Autowired
    private GestorEspacoValidator validator;

    @Autowired
    private CadastrarGestorEspaco cadastrarGestorEspaco;

    @Autowired
    private ReativarGestorEspaco reativarGestorEspaco;

    public GestorEspacoRetornoDTO executar(GestorEspacoDTO data) {
        var gestorInativoOpt = repository.findByUsuarioGestorIdAndEspacoIdAndEstaAtivoFalse(data.usuarioGestorId(), data.espacoId());

        if (gestorInativoOpt.isPresent()) {
            return reativarGestorEspaco.reativar(gestorInativoOpt.get());
        }

        return cadastrarGestorEspaco.cadastrarGestorEspaco(data);
    }
}
