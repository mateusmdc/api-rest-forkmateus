package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa o relacionamento entre um usuário gestor e um complexo de espaços.
 * Gestores de complexos têm permissão para:
 * - Atualizar informações do complexo (nome, descrição, site)
 * - Adicionar e remover espaços do complexo
 */
@Table(name = "gestor_complexo_espacos")
@Entity(name = "GestorComplexoEspacos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class GestorComplexoEspacos {
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_gestor_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuarioGestor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "complexo_espacos_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ComplexoEspacos complexoEspacos;

    @Column(name = "esta_ativo", nullable = false)
    private Boolean estaAtivo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public GestorComplexoEspacos(Usuario usuarioGestor, ComplexoEspacos complexoEspacos) {
        this.usuarioGestor = usuarioGestor;
        this.complexoEspacos = complexoEspacos;
        this.estaAtivo = true;
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
        if (estaAtivo != null && !estaAtivo) {
            this.deletedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        if (estaAtivo != null && !estaAtivo && deletedAt == null) {
            this.deletedAt = LocalDateTime.now();
        }
        if (estaAtivo != null && estaAtivo && deletedAt != null) {
            this.deletedAt = null;
        }
    }
}
