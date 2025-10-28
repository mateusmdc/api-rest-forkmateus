package br.uece.alunos.sisreserva.v1.domain.tipoAtividade;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "tipo_atividade")
@Entity(name = "TipoAtividade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TipoAtividade {
    @Id
    @Column(name = "id", nullable = false, length=36, updatable=false)
    private String id;

    @NotNull
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * Relacionamento ManyToMany com Espaco (lado inverso).
     * Um tipo de atividade pode estar em múltiplos espaços.
     */
    @ManyToMany(mappedBy = "tiposAtividade", fetch = FetchType.LAZY)
    private List<Espaco> espacos = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
