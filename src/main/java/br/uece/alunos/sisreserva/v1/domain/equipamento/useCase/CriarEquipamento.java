package br.uece.alunos.sisreserva.v1.domain.equipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.equipamento.EquipamentoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamento.validation.EquipamentoValidator;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.espaco.validation.EspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável pela criação de equipamento.
 * Realiza validações de permissão e dados, e opcionalmente vincula o equipamento a um espaço.
 */
@Component
public class CriarEquipamento {

    @Autowired
    private EquipamentoRepository repository;

    @Autowired
    private EquipamentoEspacoRepository equipamentoEspacoRepository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private EquipamentoValidator equipamentoValidator;

    @Autowired
    private EspacoValidator espacoValidator;

    /**
     * Cria um novo equipamento no sistema.
     * Apenas administradores podem criar equipamentos.
     * Opcionalmente cria um vínculo com um espaço se espacoId for fornecido.
     * 
     * @param data dados do equipamento a ser criado
     * @return DTO com os dados do equipamento criado
     */
    public EquipamentoRetornoDTO criar(EquipamentoDTO data) {
        // Valida permissão de administrador
        equipamentoValidator.validarPermissaoParaCriar();

        TipoEquipamento tipoEquipamento = entityHandlerService.obterTipoEquipamentoPorId(data.tipoEquipamentoId());

        equipamentoValidator.validarDadosObrigatorios(data, tipoEquipamento);
        equipamentoValidator.validarTombamentoUnico(data.tombamento());

        // Cria e salva o equipamento
        Equipamento novoEquipamento = new Equipamento(data, tipoEquipamento);
        var equipamentoNoBanco = repository.save(novoEquipamento);

        // Se um espaço foi informado, cria o vínculo
        if (data.espacoId() != null && !data.espacoId().trim().isEmpty()) {
            espacoValidator.validarEspacoId(data.espacoId());
            Espaco espaco = entityHandlerService.obterEspacoPorId(data.espacoId());
            
            EquipamentoEspaco equipamentoEspaco = new EquipamentoEspaco(equipamentoNoBanco, espaco);
            equipamentoEspacoRepository.save(equipamentoEspaco);
        }

        return new EquipamentoRetornoDTO(equipamentoNoBanco);
    }
}
