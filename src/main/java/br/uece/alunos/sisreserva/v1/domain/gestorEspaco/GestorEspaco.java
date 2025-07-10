package br.uece.alunos.sisreserva.v1.domain.gestorEspaco;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "gestor_espaco")
@Entity(name = "GestorEspaco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class GestorEspaco {
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
    @JoinColumn(name = "espaco_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Espaco espaco;

    @Column(name = "esta_ativo", nullable = false)
    private Boolean estaAtivo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public GestorEspaco(Usuario usuarioGestor, Espaco espaco) {
        this.usuarioGestor = usuarioGestor;
        this.espaco = espaco;
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
