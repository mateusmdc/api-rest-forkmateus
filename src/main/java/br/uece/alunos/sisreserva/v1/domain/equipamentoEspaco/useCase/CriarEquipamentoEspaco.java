package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase.ValidadorGestorEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.CriarEquipamentoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoEspaco.EquipamentoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CriarEquipamentoEspaco {
    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private EquipamentoEspacoRepository repository;

    @Autowired
    private EquipamentoService equipamentoService;

    @Autowired
    private ValidadorGestorEspaco validaSeGestorEspaco;

    public EquipamentoEspacoRetornoDTO criarEquipamentoAlocandoAoEspaco(CriarEquipamentoEspacoDTO data) {
        // inicialmente valida se o usuário que está tentando criar o equipamento, alocando ao espaço, é gestor daquele espaço
        validaSeGestorEspaco.validarGestorAtivo(data.usuarioId(), data.espacoId());

        var equipamentoNovo = equipamentoService.criar(data.equipamento());

        var equipamentoEntidade = entityHandlerService.obterEquipamentoPorId(equipamentoNovo.id());

        var espacoEntidade =  entityHandlerService.obterEspacoPorId(data.espacoId());

        var equipamentoEspaco = new EquipamentoEspaco(equipamentoEntidade, espacoEntidade);

        var equipamentoEspacoNoBanco = repository.save(equipamentoEspaco);

        return new EquipamentoEspacoRetornoDTO(equipamentoEspacoNoBanco);
    }
}
