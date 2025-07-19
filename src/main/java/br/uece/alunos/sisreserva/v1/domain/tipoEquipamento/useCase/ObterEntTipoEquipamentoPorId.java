package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamentoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterEntTipoEquipamentoPorId {
    @Autowired
    private TipoEquipamentoRepository repository;

    public TipoEquipamento obterEntidadePorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foi encontrado tipo de equipamento com o ID informado."));
    }
}
