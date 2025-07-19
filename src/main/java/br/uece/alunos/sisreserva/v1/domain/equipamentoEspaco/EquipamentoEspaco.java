package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "equipamento_espaco")
@Entity(name = "EquipamentoEspaco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EquipamentoEspaco {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "espaco_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Espaco espaco;

    @Column(name = "data_alocacao", updatable = false)
    private LocalDateTime dataAlocacao;

    @Column(name = "data_remocao")
    private LocalDateTime dataRemocao;

    public EquipamentoEspaco(Equipamento equipamento, Espaco espaco) {
        this.equipamento = equipamento;
        this.espaco = espaco;
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.dataAlocacao = LocalDateTime.now();
    }
}