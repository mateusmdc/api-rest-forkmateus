package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation.GestorEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InativarGestorEspaco {
    private final GestorEspacoRepository repository;
    private final GestorEspacoValidator validator;

    public GestorEspacoRetornoDTO inativar(String gestorEspacoId) {
        validator.validarGestorAtivoParaInativar(gestorEspacoId);

        var gestor = repository.findById(gestorEspacoId)
                        .orElseThrow(() -> new IllegalStateException("Gestor de Espaço deveria existir após validação, mas não foi encontrado."));

        gestor.setEstaAtivo(false);

        var gestorInativado = repository.save(gestor);

        return new GestorEspacoRetornoDTO(gestorInativado);
    }

}
