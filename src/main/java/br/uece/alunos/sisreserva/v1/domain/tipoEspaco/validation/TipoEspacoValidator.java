package br.uece.alunos.sisreserva.v1.domain.tipoEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TipoEspacoValidator {
    private final TipoEspacoRepository repository;

    public void validarSeTipoEspacoJaExiste(String nome) {
        var tipoEspacoExistente = repository.findByNomeIgnoreCase(nome);
        if (tipoEspacoExistente.isPresent()) {
            throw new ValidationException("Já existe um tipo de espaço com este nome");
        }
    }

    public void validarSeTipoEspacoJaExisteParaAtualizacao(String id, String nome) {
        if (nome == null || nome.isBlank()) {
            return;
        }
        var tipoEspacoExistente = repository.findByNomeIgnoreCase(nome);
        if (tipoEspacoExistente.isPresent() && !tipoEspacoExistente.get().getId().equals(id)) {
            throw new ValidationException("Já existe outro tipo de espaço com este nome");
        }
    }
}
