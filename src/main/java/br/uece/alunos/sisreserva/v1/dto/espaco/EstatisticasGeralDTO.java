package br.uece.alunos.sisreserva.v1.dto.espaco;

import java.util.List;

/**
 * DTO para resposta de estatísticas gerais de uso dos espaços.
 * 
 * <p>Agrupa as estatísticas de múltiplos espaços em uma única resposta.</p>
 * 
 * @param espacos lista de estatísticas por espaço
 */
public record EstatisticasGeralDTO(
    List<EstatisticasEspacoDTO> espacos
) {}
