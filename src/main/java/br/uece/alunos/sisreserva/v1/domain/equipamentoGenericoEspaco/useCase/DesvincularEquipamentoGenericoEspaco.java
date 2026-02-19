package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.validation.EquipamentoGenericoEspacoValidator;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por desvincular equipamento genérico de um espaço.
 * Valida permissões antes de realizar a remoção do vínculo.
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
public class DesvincularEquipamentoGenericoEspaco {

    private final EquipamentoGenericoEspacoRepository repository;
    private final EquipamentoGenericoEspacoValidator validator;

    /**
     * Remove o vínculo entre equipamento genérico e espaço.
     * 
     * @param vinculoId ID do vínculo equipamento-espaço
     * @throws ValidationException se o vínculo não existir ou usuário não tiver permissão
     */
    public void desvincular(String vinculoId) {
        // Valida se o vínculo existe
        validator.validarVinculoExiste(vinculoId);

        // Busca o vínculo para validar permissão
        EquipamentoGenericoEspaco vinculo = repository.findById(vinculoId)
            .orElseThrow(() -> new ValidationException("Vínculo equipamento-espaço não encontrado."));

        // Valida permissão do usuário para gerenciar equipamentos do espaço
        validator.validarPermissaoParaGerenciarEquipamentos(vinculo.getEspaco().getId());

        // Remove o vínculo
        repository.deleteById(vinculoId);
    }
}
