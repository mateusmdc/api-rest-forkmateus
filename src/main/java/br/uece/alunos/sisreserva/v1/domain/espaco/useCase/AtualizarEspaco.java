package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class AtualizarEspaco {
    private final EspacoRepository repository;
    private final EspacoValidator validator;

    @Transactional
    public EspacoRetornoDTO atualizar(String id, EspacoAtualizarDTO data) {
        validator.validarEspacoId(id);

        var espaco = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado, mesmo após a validação do ID."));

        espaco.atualizar(data);

        var salvo = repository.save(espaco);

        return new EspacoRetornoDTO(salvo);
    }
}
