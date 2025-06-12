package br.uece.alunos.api_aluga_espacos.v1.domain.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import br.uece.alunos.api_aluga_espacos.v1.domain.instituicao.Instituicao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "usuario")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @Column(name = "id", nullable = false, length=36, updatable=false)
    private String id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "matricula", nullable = false)
    private Integer matricula;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "foto_perfil", length = 100)
    private String fotoPerfil;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instituicao_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Instituicao instituicao;

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

    @Column(name = "refresh_token_enabled")
    private boolean refreshTokenEnabled = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !lockoutEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !lockoutEnabled;
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}