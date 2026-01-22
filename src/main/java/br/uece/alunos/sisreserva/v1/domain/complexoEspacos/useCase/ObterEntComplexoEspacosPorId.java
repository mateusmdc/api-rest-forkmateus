package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Use case para obter um complexo de espaços por ID.
 * Utilizado pelo EntityHandlerService para validar e buscar entidades.
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObterEntComplexoEspacosPorId {
    private final ComplexoEspacosRepository repository;

    public ComplexoEspacos obterEntidade(String id) {
        log.debug("Buscando complexo de espaços com ID: {}", id);
        
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Complexo de espaços não encontrado - ID: {}", id);
                    return new ValidationException("Complexo de Espaços não encontrado");
                });
    }
}
