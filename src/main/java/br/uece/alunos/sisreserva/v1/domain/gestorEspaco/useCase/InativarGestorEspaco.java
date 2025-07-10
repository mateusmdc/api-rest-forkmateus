package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation.GestorEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InativarGestorEspaco {
    @Autowired
    private GestorEspacoRepository repository;
    @Autowired
    private GestorEspacoValidator validator;

    public GestorEspacoRetornoDTO inativar(String gestorEspacoId) {
        validator.validarGestorAtivoParaInativar(gestorEspacoId);

        var gestor = repository.findById(gestorEspacoId).get();

        gestor.setEstaAtivo(false);

        var gestorInativado = repository.save(gestor);

        return new GestorEspacoRetornoDTO(gestorInativado);
    }

}
