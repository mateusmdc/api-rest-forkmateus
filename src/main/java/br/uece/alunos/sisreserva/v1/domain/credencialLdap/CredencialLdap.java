package br.uece.alunos.sisreserva.v1.domain.credencialLdap;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "credencial_ldap")
@Entity(name = "CredencialLdap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CredencialLdap {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "ldap_username", nullable = false, length = 100, unique = true)
    private String ldapUsername;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public CredencialLdap(Usuario usuario, String ldapUsername) {
        this.usuario = usuario;
        this.ldapUsername = ldapUsername;
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }
}