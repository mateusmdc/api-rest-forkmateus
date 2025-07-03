package br.uece.alunos.sisreserva.v1.domain.equipamento;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "equipamento")
@Entity(name = "Equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Equipamento {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @NotNull
    @Size(max = 100)
    @Column(name = "tombamento", nullable = false, unique = true, length = 100)
    private String tombamento;

    @Size(max = 255)
    @Column(name = "descricao", length = 255)
    private String descricao;

    /**
     * 0 = Inativo, 1 = Ativo, 2 = Em manutenção
     */
    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_equipamento_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoEquipamento tipoEquipamento;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
