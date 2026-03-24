package br.uece.alunos.sisreserva.v1.domain.credencialLocal;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "credencial_local")
@Entity(name = "CredencialLocal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CredencialLocal {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CredencialLocal(Usuario usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

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