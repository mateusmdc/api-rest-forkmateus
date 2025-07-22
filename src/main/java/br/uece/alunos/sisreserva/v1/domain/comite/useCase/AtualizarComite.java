package br.uece.alunos.sisreserva.v1.domain.comite.useCase;

import br.uece.alunos.sisreserva.v1.domain.comite.ComiteRepository;
import br.uece.alunos.sisreserva.v1.domain.comite.validation.ComiteValidator;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarComite {
    private final ComiteRepository repository;
    private final ComiteValidator validator;

    public ComiteRetornoDTO atualizar(ComiteAtualizarDTO data, String comiteId) {
            validator.validarComiteId(comiteId);

            var comite = repository.findById(comiteId)
                .orElseThrow(() -> new IllegalStateException("Comitê deveria existir após validação do ID, mas não foi encontrado."));

            comite.atualizarComite(data);

            validator.validarDescricaoDuplicada(new ComiteDTO(comite.getDescricao(), comite.getTipo()));

            var comiteAtualizado = repository.save(comite);

            return new ComiteRetornoDTO(comiteAtualizado);
    }
}
