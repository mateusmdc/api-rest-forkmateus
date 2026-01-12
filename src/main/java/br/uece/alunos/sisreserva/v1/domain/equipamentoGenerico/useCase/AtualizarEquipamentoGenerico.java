package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenericoRepository;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para atualizar um equipamento genérico existente.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class AtualizarEquipamentoGenerico {

    private final EquipamentoGenericoRepository repository;
    private final ObterEquipamentoGenericoPorId obterEquipamentoGenericoPorId;

    /**
     * Atualiza um equipamento genérico existente.
     * Valida se o novo nome já existe em outro equipamento.
     * 
     * @param id ID do equipamento genérico a ser atualizado
     * @param dto DTO contendo os novos dados
     * @return Equipamento genérico atualizado
     * @throws ValidationException se já existir outro equipamento com o mesmo nome
     */
    public EquipamentoGenerico atualizar(String id, EquipamentoGenericoDTO dto) {
        log.info("Atualizando equipamento genérico ID: {}", id);
        
        EquipamentoGenerico equipamento = obterEquipamentoGenericoPorId.obter(id);
        
        // Valida se já existe outro equipamento com o mesmo nome (excluindo o próprio)
        if (repository.existsByNomeIgnoreCaseAndTrimmedExcludingId(dto.nome(), id)) {
            log.warn("Tentativa de atualizar equipamento genérico com nome duplicado: {}", dto.nome());
            throw new ValidationException("Já existe outro equipamento genérico com o nome: " + dto.nome());
        }
        
        equipamento.atualizar(dto);
        EquipamentoGenerico updated = repository.save(equipamento);
        
        log.info("Equipamento genérico atualizado com sucesso. ID: {}", updated.getId());
        return updated;
    }
}
