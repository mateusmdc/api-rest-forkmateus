package br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para criação e atualização de equipamentos genéricos.
 * 
 * @param nome Nome do equipamento genérico (obrigatório)
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public record EquipamentoGenericoDTO(
        @NotBlank(message = "O nome do equipamento genérico é obrigatório")
        String nome
) {
}
