package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento;

import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "tipo_equipamento")
@Entity(name = "TipoEquipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TipoEquipamento {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @NotNull
    @Column(name = "nome", nullable = false, length = 100, unique = true)
    private String nome;

    @Column(name = "is_detalhamento_obrigatorio", nullable = false)
    private Boolean isDetalhamentoObrigatorio = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    public TipoEquipamento(TipoEquipamentoDTO data) {
        this.nome = data.nome();
        this.isDetalhamentoObrigatorio = data.isDetalhamentoObrigatorio();
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    public void atualizar(TipoEquipamentoAtualizarDTO data) {
        if (data.nome() != null && !data.nome().isEmpty()) {
            this.nome = data.nome();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
