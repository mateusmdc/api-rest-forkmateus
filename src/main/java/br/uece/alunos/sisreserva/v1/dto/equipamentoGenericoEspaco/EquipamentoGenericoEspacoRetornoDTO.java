package br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.EquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;

import java.time.LocalDateTime;

/**
 * DTO de retorno para o relacionamento equipamento genérico - espaço.
 * Contém as informações do vínculo e dados do equipamento genérico.
 * 
 * @param id identificador do vínculo
 * @param equipamentoGenerico dados do equipamento genérico
 * @param espaco dados do espaço
 * @param quantidade quantidade do equipamento no espaço
 * @param dataVinculo data de criação do vínculo
 * @param dataAtualizacao data da última atualização
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public record EquipamentoGenericoEspacoRetornoDTO(
    String id,
    EquipamentoGenericoRetornoDTO equipamentoGenerico,
    EspacoRetornoDTO espaco,
    Integer quantidade,
    LocalDateTime dataVinculo,
    LocalDateTime dataAtualizacao
) {
    /**
     * Construtor que converte a entidade para DTO.
     * 
     * @param equipamentoGenericoEspaco entidade a ser convertida
     */
    public EquipamentoGenericoEspacoRetornoDTO(EquipamentoGenericoEspaco equipamentoGenericoEspaco) {
        this(
            equipamentoGenericoEspaco.getId(),
            new EquipamentoGenericoRetornoDTO(equipamentoGenericoEspaco.getEquipamentoGenerico()),
            new EspacoRetornoDTO(equipamentoGenericoEspaco.getEspaco()),
            equipamentoGenericoEspaco.getQuantidade(),
            equipamentoGenericoEspaco.getDataVinculo(),
            equipamentoGenericoEspaco.getDataAtualizacao()
        );
    }
}
