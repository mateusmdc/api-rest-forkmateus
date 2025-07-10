package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspaco;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReativarGestorEspaco {
    @Autowired
    private GestorEspacoRepository repository;

    public GestorEspacoRetornoDTO reativar(GestorEspaco gestorInativo) {
        gestorInativo.setEstaAtivo(true);
        gestorInativo.setDeletedAt(null);

        var gestorReativadoNoBanco = repository.save(gestorInativo);

        return new GestorEspacoRetornoDTO(gestorReativadoNoBanco);
    }
}
