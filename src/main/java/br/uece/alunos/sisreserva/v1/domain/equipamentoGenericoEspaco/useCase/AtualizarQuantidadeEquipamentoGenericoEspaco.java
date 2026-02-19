package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.validation.EquipamentoGenericoEspacoValidator;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.AtualizarQuantidadeDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por atualizar a quantidade de um equipamento genérico em um espaço.
 * Valida permissões antes de realizar a atualização.
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
public class AtualizarQuantidadeEquipamentoGenericoEspaco {

    private final EquipamentoGenericoEspacoRepository repository;
    private final EquipamentoGenericoEspacoValidator validator;

    /**
     * Atualiza a quantidade de um equipamento genérico em um espaço.
     * 
     * @param vinculoId ID do vínculo equipamento-espaço
     * @param dto dados com a nova quantidade
     * @return entidade EquipamentoGenericoEspaco atualizada
     * @throws ValidationException se o vínculo não existir ou usuário não tiver permissão
     */
    public EquipamentoGenericoEspaco atualizarQuantidade(String vinculoId, AtualizarQuantidadeDTO dto) {
        // Valida se o vínculo existe
        validator.validarVinculoExiste(vinculoId);

        // Busca o vínculo
        EquipamentoGenericoEspaco vinculo = repository.findById(vinculoId)
            .orElseThrow(() -> new ValidationException("Vínculo equipamento-espaço não encontrado."));

        // Valida permissão do usuário para gerenciar equipamentos do espaço
        validator.validarPermissaoParaGerenciarEquipamentos(vinculo.getEspaco().getId());

        // Valida quantidade
        validator.validarQuantidade(dto.quantidade());

        // Atualiza a quantidade
        vinculo.setQuantidade(dto.quantidade());

        return repository.save(vinculo);
    }
}
