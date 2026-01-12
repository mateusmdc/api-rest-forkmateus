package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenericoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para obter um equipamento genérico específico por ID.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObterEquipamentoGenericoPorId {

    private final EquipamentoGenericoRepository repository;

    /**
     * Obtém um equipamento genérico por seu ID.
     * 
     * @param id ID do equipamento genérico
     * @return Equipamento genérico encontrado
     * @throws ValidationException se o equipamento genérico não for encontrado
     */
    public EquipamentoGenerico obter(String id) {
        log.debug("Buscando equipamento genérico por ID: {}", id);
        
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Equipamento genérico não encontrado com ID: {}", id);
                    return new ValidationException("Equipamento genérico não encontrado");
                });
    }
}
