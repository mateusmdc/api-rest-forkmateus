package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamento.validation.EquipamentoValidator;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarEquipamento {

    @Autowired
    private EquipamentoRepository repository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private EquipamentoValidator equipamentoValidator;

    public EquipamentoRetornoDTO criar(EquipamentoDTO data) {
        TipoEquipamento tipoEquipamento = entityHandlerService.obterTipoEquipamentoPorId(data.tipoEquipamentoId());

        equipamentoValidator.validarDadosObrigatorios(data, tipoEquipamento);
        equipamentoValidator.validarTombamentoUnico(data.tombamento());

        Equipamento novoEquipamento = new Equipamento(data, tipoEquipamento);
        var equipamentoNoBanco = repository.save(novoEquipamento);

        return new EquipamentoRetornoDTO(equipamentoNoBanco);
    }
}
