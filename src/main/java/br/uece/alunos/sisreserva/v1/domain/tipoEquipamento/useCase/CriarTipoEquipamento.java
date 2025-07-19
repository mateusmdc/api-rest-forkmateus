package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.validation.TipoEquipamentoValidator;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarTipoEquipamento {

    @Autowired
    private TipoEquipamentoRepository repository;

    @Autowired
    private TipoEquipamentoValidator validator;

    public TipoEquipamentoRetornoDTO criar(TipoEquipamentoDTO data) {
        validator.validarNomeDuplicado(data);

        TipoEquipamento tipoEquipamento = new TipoEquipamento(data);
        var salvo = repository.save(tipoEquipamento);

        return new TipoEquipamentoRetornoDTO(salvo);
    }
}
