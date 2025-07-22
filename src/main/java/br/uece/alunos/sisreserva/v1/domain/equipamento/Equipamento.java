package br.uece.alunos.sisreserva.v1.domain.equipamento;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
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

    @Size(max = 100)
    @Column(name = "tombamento", length = 100)
    private String tombamento;

    @Size(max = 255)
    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusEquipamento status = StatusEquipamento.ATIVO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_equipamento_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoEquipamento tipoEquipamento;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Equipamento(EquipamentoDTO data, TipoEquipamento tipoEquipamento) {
        this.tombamento = data.tombamento();
        this.descricao = data.descricao();
        this.status = data.statusEquipamento();
        this.tipoEquipamento =  tipoEquipamento;
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    public void atualizar(EquipamentoAtualizarDTO data) {
        if (data.descricao() != null) {
            this.descricao = data.descricao();
        }
        if (data.status() != null) {
            this.status = data.status();
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
