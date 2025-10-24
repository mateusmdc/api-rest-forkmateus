package br.uece.alunos.sisreserva.v1.domain.complexoEspacos;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "complexo_espacos")
@Entity(name = "ComplexoEspacos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ComplexoEspacos {
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @NotNull
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "site", length = 255)
    private String site;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "complexo_espacos_espaco",
        joinColumns = @JoinColumn(name = "complexo_espacos_id"),
        inverseJoinColumns = @JoinColumn(name = "espaco_id")
    )
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

    public void atualizar(br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosAtualizarDTO data) {
        if (data.nome() != null && !data.nome().isBlank()) {
            this.nome = data.nome();
        }
        if (data.descricao() != null) {
            this.descricao = data.descricao();
        }
        if (data.site() != null) {
            this.site = data.site();
        }
    }
}
