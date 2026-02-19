package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRetornoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso responsável por obter os equipamentos genéricos de um espaço.
 * Lista todos os equipamentos vinculados ao espaço com suas quantidades.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ObterEquipamentosGenericosEspaco {

    private final EquipamentoGenericoEspacoRepository repository;

    /**
     * Obtém todos os equipamentos genéricos vinculados a um espaço.
     * 
     * @param espacoId ID do espaço
     * @return lista de DTOs com os equipamentos e suas quantidades
     */
    public List<EquipamentoGenericoEspacoRetornoDTO> obterPorEspaco(String espacoId) {
        List<EquipamentoGenericoEspaco> vinculos = 
            repository.findByEspacoIdWithEquipamento(espacoId);

        return vinculos.stream()
            .map(EquipamentoGenericoEspacoRetornoDTO::new)
            .collect(Collectors.toList());
    }

    /**
     * Obtém todos os espaços que possuem um equipamento genérico específico.
     * 
     * @param equipamentoGenericoId ID do equipamento genérico
     * @return lista de DTOs com os espaços e quantidades
     */
    public List<EquipamentoGenericoEspacoRetornoDTO> obterPorEquipamento(String equipamentoGenericoId) {
        List<EquipamentoGenericoEspaco> vinculos = 
            repository.findByEquipamentoGenericoIdWithEspaco(equipamentoGenericoId);

        return vinculos.stream()
            .map(EquipamentoGenericoEspacoRetornoDTO::new)
            .collect(Collectors.toList());
    }
}
