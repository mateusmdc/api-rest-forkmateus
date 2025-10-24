package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.useCase;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeletarComplexoEspacos {
    private final ComplexoEspacosRepository repository;

    public void deletar(String id) {
        var complexo = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Complexo de espaços não encontrado"));
        
        repository.delete(complexo);
    }
}
