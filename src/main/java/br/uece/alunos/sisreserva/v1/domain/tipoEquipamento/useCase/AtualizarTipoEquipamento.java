package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.validator.TipoEquipamentoValidator;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarTipoEquipamento {
    private final TipoEquipamentoRepository repository;
    private final TipoEquipamentoValidator validator;

    public TipoEquipamentoRetornoDTO atualizar(TipoEquipamentoAtualizarDTO data, String tipoEquipamentoId) {
        validator.validarTipoEquipamentoId(tipoEquipamentoId);

        var tipoEquipamento = repository.findById(tipoEquipamentoId)
                .orElseThrow(() -> new IllegalStateException("Tipo Comitê não encontrado, mesmo após a validação do ID."));

        tipoEquipamento.atualizar(data);

        var atualizadoNoBanco = repository.save(tipoEquipamento);

        return new TipoEquipamentoRetornoDTO(atualizadoNoBanco);
    }

}
