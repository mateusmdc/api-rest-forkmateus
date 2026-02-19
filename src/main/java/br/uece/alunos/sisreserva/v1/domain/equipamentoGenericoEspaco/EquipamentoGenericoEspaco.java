package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.EquipamentoGenerico;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa o relacionamento entre Equipamento Genérico e Espaço.
 * Registra as quantidades de equipamentos genéricos alocados em cada espaço.
 * 
 * <p>Exemplos de uso:</p>
 * <ul>
 *   <li>Espaço "Laboratório A" possui 30 Cadeiras</li>
 *   <li>Espaço "Auditório" possui 1 Projetor</li>
 *   <li>Espaço "Sala 101" possui 40 Carteiras</li>
 * </ul>
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Table(name = "equipamento_generico_espaco")
@Entity(name = "EquipamentoGenericoEspaco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EquipamentoGenericoEspaco {

    /**
     * Identificador único do relacionamento equipamento genérico - espaço.
     */
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    /**
     * Equipamento genérico vinculado ao espaço.
     */
    @NotNull(message = "O equipamento genérico é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_generico_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EquipamentoGenerico equipamentoGenerico;

    /**
     * Espaço onde o equipamento genérico está alocado.
     */
    @NotNull(message = "O espaço é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "espaco_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Espaco espaco;

    /**
     * Quantidade do equipamento genérico presente no espaço.
     * Deve ser no mínimo 1.
     */
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    /**
     * Data e hora em que o equipamento genérico foi vinculado ao espaço.
     */
    @Column(name = "data_vinculo", updatable = false)
    private LocalDateTime dataVinculo;

    /**
     * Data e hora da última atualização da quantidade.
     */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Construtor para criar um novo vínculo equipamento genérico - espaço.
     * 
     * @param equipamentoGenerico equipamento genérico a ser vinculado
     * @param espaco espaço onde o equipamento será alocado
     * @param quantidade quantidade do equipamento no espaço
     */
    public EquipamentoGenericoEspaco(EquipamentoGenerico equipamentoGenerico, Espaco espaco, Integer quantidade) {
        this.equipamentoGenerico = equipamentoGenerico;
        this.espaco = espaco;
        this.quantidade = quantidade;
    }

    /**
     * Inicializa o ID e a data de vínculo antes de persistir no banco de dados.
     */
    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.dataVinculo = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Atualiza a data de atualização antes de modificar no banco de dados.
     */
    @PreUpdate
    public void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
