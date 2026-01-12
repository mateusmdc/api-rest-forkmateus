package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenericoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para deletar um equipamento genérico.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class DeletarEquipamentoGenerico {

    private final EquipamentoGenericoRepository repository;
    private final ObterEquipamentoGenericoPorId obterEquipamentoGenericoPorId;

    /**
     * Deleta um equipamento genérico do sistema.
     * 
     * @param id ID do equipamento genérico a ser deletado
     */
    public void deletar(String id) {
        log.info("Deletando equipamento genérico ID: {}", id);
        
        EquipamentoGenerico equipamento = obterEquipamentoGenericoPorId.obter(id);
        repository.delete(equipamento);
        
        log.info("Equipamento genérico deletado com sucesso. ID: {}", id);
    }
}
