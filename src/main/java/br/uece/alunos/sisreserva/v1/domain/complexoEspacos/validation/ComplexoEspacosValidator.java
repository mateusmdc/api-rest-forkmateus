package br.uece.alunos.sisreserva.v1.domain.complexoEspacos.validation;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacosRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ComplexoEspacosValidator {
    private final ComplexoEspacosRepository repository;

    public void validarSeComplexoJaExiste(String nome) {
        var complexoExistente = repository.findByNomeIgnoreCase(nome);
        if (complexoExistente.isPresent()) {
            throw new ValidationException("Já existe um complexo de espaços com este nome");
        }
    }

    public void validarSeComplexoJaExisteParaAtualizacao(String id, String nome) {
        if (nome == null || nome.isBlank()) {
            return;
        }
        var complexoExistente = repository.findByNomeIgnoreCase(nome);
        if (complexoExistente.isPresent() && !complexoExistente.get().getId().equals(id)) {
            throw new ValidationException("Já existe outro complexo de espaços com este nome");
        }
    }
}
