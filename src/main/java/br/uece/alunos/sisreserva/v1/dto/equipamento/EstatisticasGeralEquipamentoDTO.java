package br.uece.alunos.sisreserva.v1.dto.equipamento;

import java.util.List;

/**
 * DTO para resposta de estatísticas gerais de uso dos equipamentos.
 * 
 * <p>Agrupa as estatísticas de múltiplos equipamentos em uma única resposta.</p>
 * 
 * @param equipamentos lista de estatísticas por equipamento
 */
public record EstatisticasGeralEquipamentoDTO(
    List<EstatisticasEquipamentoDTO> equipamentos
) {}
