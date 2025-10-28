package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
public class AtualizarEspaco {
    private final EspacoRepository repository;
    private final EspacoValidator validator;
    private final EntityHandlerService entityHandlerService;

    @Transactional
    public EspacoRetornoDTO atualizar(String id, EspacoAtualizarDTO data) {
        validator.validarEspacoId(id);

        var espaco = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado, mesmo após a validação do ID."));

        // Atualizar campos básicos do espaço
        espaco.atualizar(data);

        // Atualizar tipos de atividade se fornecidos
        if (data.tipoAtividadeIds() != null) {
            List<TipoAtividade> tiposAtividade = data.tipoAtividadeIds().stream()
                    .map(entityHandlerService::obterTipoAtividadePorId)
                    .toList();
            espaco.atualizarTiposAtividade(tiposAtividade);
        }

        var salvo = repository.save(espaco);

        return new EspacoRetornoDTO(salvo);
    }
}
