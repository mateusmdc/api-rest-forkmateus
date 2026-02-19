package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.AtualizarQuantidadeDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRetornoDTO;

import java.util.List;

/**
 * Interface de serviço para gerenciamento do relacionamento entre equipamentos genéricos e espaços.
 * Define as operações disponíveis para vincular, desvincular e consultar equipamentos em espaços.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public interface EquipamentoGenericoEspacoService {

    /**
     * Vincula um equipamento genérico a um espaço com uma quantidade específica.
     * Apenas administradores, gestores e secretarias do espaço podem realizar esta operação.
     * 
     * @param dto dados do vínculo (equipamento, espaço e quantidade)
     * @return DTO com os dados do vínculo criado
     */
    EquipamentoGenericoEspacoRetornoDTO vincular(EquipamentoGenericoEspacoDTO dto);

    /**
     * Atualiza a quantidade de um equipamento genérico em um espaço.
     * Apenas administradores, gestores e secretarias do espaço podem realizar esta operação.
     * 
     * @param vinculoId ID do vínculo equipamento-espaço
     * @param dto dados com a nova quantidade
     * @return DTO com os dados do vínculo atualizado
     */
    EquipamentoGenericoEspacoRetornoDTO atualizarQuantidade(String vinculoId, AtualizarQuantidadeDTO dto);

    /**
     * Remove o vínculo entre equipamento genérico e espaço.
     * Apenas administradores, gestores e secretarias do espaço podem realizar esta operação.
     * 
     * @param vinculoId ID do vínculo equipamento-espaço
     */
    void desvincular(String vinculoId);

    /**
     * Obtém todos os equipamentos genéricos vinculados a um espaço.
     * 
     * @param espacoId ID do espaço
     * @return lista de DTOs com os equipamentos e suas quantidades
     */
    List<EquipamentoGenericoEspacoRetornoDTO> obterPorEspaco(String espacoId);

    /**
     * Obtém todos os espaços que possuem um equipamento genérico específico.
     * 
     * @param equipamentoGenericoId ID do equipamento genérico
     * @return lista de DTOs com os espaços e quantidades
     */
    List<EquipamentoGenericoEspacoRetornoDTO> obterPorEquipamento(String equipamentoGenericoId);
}
