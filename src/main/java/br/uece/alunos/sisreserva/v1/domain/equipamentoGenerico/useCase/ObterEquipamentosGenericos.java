package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenericoRepository;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.specification.EquipamentoGenericoSpecification;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoRetornoDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para obter equipamentos genéricos com filtros e paginação.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ObterEquipamentosGenericos {

    private final EquipamentoGenericoRepository repository;

    /**
     * Obtém equipamentos genéricos com filtros e paginação.
     * 
     * @param pageable Informações de paginação
     * @param id Filtro por ID do equipamento genérico
     * @param nome Filtro por nome do equipamento genérico (busca parcial)
     * @return Página com os equipamentos genéricos encontrados
     */
    public Page<EquipamentoGenericoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        log.debug("Buscando equipamentos genéricos - ID: {}, Nome: {}", id, nome);
        
        var spec = EquipamentoGenericoSpecification.byFilters(id, nome);
        var page = repository.findAll(spec, pageable);
        
        log.debug("Encontrados {} equipamentos genéricos", page.getTotalElements());
        return page.map(EquipamentoGenericoRetornoDTO::new);
    }
}
