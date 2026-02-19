package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.validation.EquipamentoGenericoEspacoValidator;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por vincular equipamento genérico a um espaço.
 * Valida permissões e registra a quantidade do equipamento no espaço.
 * 
 * <p>Permissões necessárias:</p>
 * <ul>
 *   <li>Administrador do sistema</li>
 *   <li>Gestor ativo do espaço</li>
 *   <li>Secretaria ativa do espaço</li>
 * </ul>
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class VincularEquipamentoGenericoEspaco {

    private final EquipamentoGenericoEspacoRepository repository;
    private final EquipamentoGenericoEspacoValidator validator;
    private final EntityHandlerService entityHandlerService;

    /**
     * Vincula um equipamento genérico a um espaço com uma quantidade específica.
     * 
     * @param dto dados do vínculo (equipamento, espaço e quantidade)
     * @return entidade EquipamentoGenericoEspaco criada
     * @throws br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException 
     *         se o usuário não tiver permissão ou o vínculo já existir
     */
    public EquipamentoGenericoEspaco vincular(EquipamentoGenericoEspacoDTO dto) {
        // Valida permissão do usuário para gerenciar equipamentos do espaço
        validator.validarPermissaoParaGerenciarEquipamentos(dto.espacoId());

        // Valida se o vínculo já existe
        validator.validarVinculoJaExistente(dto.equipamentoGenericoId(), dto.espacoId());

        // Valida quantidade
        validator.validarQuantidade(dto.quantidade());

        // Obtém as entidades do banco de dados
        EquipamentoGenerico equipamentoGenerico = 
            entityHandlerService.obterEquipamentoGenericoPorId(dto.equipamentoGenericoId());
        
        Espaco espaco = 
            entityHandlerService.obterEspacoPorId(dto.espacoId());

        // Cria e salva o vínculo
        EquipamentoGenericoEspaco vinculo = new EquipamentoGenericoEspaco(
            equipamentoGenerico,
            espaco,
            dto.quantidade()
        );

        return repository.save(vinculo);
    }
}
