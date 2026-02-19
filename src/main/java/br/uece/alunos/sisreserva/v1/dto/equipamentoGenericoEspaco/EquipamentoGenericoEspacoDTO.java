package br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para vincular equipamento genérico a um espaço com quantidade.
 * 
 * @param equipamentoGenericoId ID do equipamento genérico (obrigatório)
 * @param espacoId ID do espaço (obrigatório)
 * @param quantidade quantidade do equipamento no espaço (min: 1)
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public record EquipamentoGenericoEspacoDTO(
    @NotBlank(message = "O ID do equipamento genérico é obrigatório")
    String equipamentoGenericoId,
    
    @NotBlank(message = "O ID do espaço é obrigatório")
    String espacoId,
    
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    Integer quantidade
) {}
