package br.uece.alunos.sisreserva.v1.domain.credencialLocal;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioEsqueciSenhaDTO;
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

    @Column(name = "access_failed_count")
    private int accessFailedCount = 0;

    @Column(name = "lockout_enabled")
    private boolean lockoutEnabled = false;

    @Column(name = "lockout_end")
    private LocalDateTime lockoutEnd;

    @Column(name = "token_expiration")
    private LocalDateTime tokenExpiration;

    @Column(name = "token_mail")
    private String tokenMail;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CredencialLocal(Usuario usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public void resetAccessCount() {
        this.accessFailedCount = 0;
        this.lockoutEnabled = false;
        this.lockoutEnd = null;
    }

    public void esqueciSenha(UsuarioEsqueciSenhaDTO data) {
        this.tokenMail = data.tokenMail();
        this.tokenExpiration = data.tokenExpiration();
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