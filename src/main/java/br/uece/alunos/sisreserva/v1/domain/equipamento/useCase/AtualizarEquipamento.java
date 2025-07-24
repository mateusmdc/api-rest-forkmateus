package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamento.validation.EquipamentoValidator;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ValidadorGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class AtualizarEquipamento {

    private final EquipamentoRepository repository;
    private final EquipamentoValidator validator;
    private final ValidadorGestorEspaco validadorGestorEspaco;

    @Transactional
    public EquipamentoRetornoDTO atualizar(String id, EquipamentoAtualizarDTO data) {
        validator.validarEquipamentoId(id);

        var equipamento = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Equipamento não encontrado, mesmo após a validação do ID."));

        var espaco = repository.findEspacoIdJoinedByEquipamentoId(id)
                .orElseThrow(() -> new ValidationException("Espaço não encontrado para o equipamento"));

        validadorGestorEspaco.validarGestorAtivo(data.usuarioId(), espaco.espacoId());

        equipamento.atualizar(data);
        var salvo = repository.save(equipamento);

        return new EquipamentoRetornoDTO(salvo);
    }
}
