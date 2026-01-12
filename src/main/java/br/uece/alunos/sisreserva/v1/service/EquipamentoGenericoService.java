package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoRetornoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface de serviço para gerenciamento de equipamentos genéricos.
 * Define as operações CRUD disponíveis para equipamentos genéricos.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public interface EquipamentoGenericoService {

    /**
     * Cria um novo equipamento genérico.
     * 
     * @param dto DTO contendo os dados do equipamento genérico
     * @return DTO com os dados do equipamento genérico criado
     */
    EquipamentoGenericoRetornoDTO criar(EquipamentoGenericoDTO dto);

    /**
     * Obtém equipamentos genéricos com filtros e paginação.
     * 
     * @param pageable Informações de paginação
     * @param id Filtro por ID
     * @param nome Filtro por nome
     * @return Página com os equipamentos genéricos encontrados
     */
    Page<EquipamentoGenericoRetornoDTO> obter(Pageable pageable, String id, String nome);

    /**
     * Obtém um equipamento genérico específico por ID.
     * 
     * @param id ID do equipamento genérico
     * @return DTO com os dados do equipamento genérico
     */
    EquipamentoGenericoRetornoDTO obterPorId(String id);

    /**
     * Atualiza um equipamento genérico existente.
     * 
     * @param id ID do equipamento genérico
     * @param dto DTO contendo os novos dados
     * @return DTO com os dados do equipamento genérico atualizado
     */
    EquipamentoGenericoRetornoDTO atualizar(String id, EquipamentoGenericoDTO dto);

    /**
     * Deleta um equipamento genérico.
     * 
     * @param id ID do equipamento genérico a ser deletado
     */
    void deletar(String id);
}
