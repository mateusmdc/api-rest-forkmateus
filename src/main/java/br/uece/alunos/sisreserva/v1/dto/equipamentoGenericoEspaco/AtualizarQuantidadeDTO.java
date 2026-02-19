package br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para atualizar a quantidade de um equipamento genérico em um espaço.
 * 
 * @param quantidade nova quantidade do equipamento (min: 1)
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public record AtualizarQuantidadeDTO(
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    Integer quantidade
) {}
