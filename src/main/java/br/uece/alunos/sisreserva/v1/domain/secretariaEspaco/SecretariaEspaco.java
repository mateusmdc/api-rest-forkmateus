package br.uece.alunos.sisreserva.v1.domain.secretariaEspaco;

import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa a relação entre um usuário da secretaria e um espaço.
 * Permite vincular usuários que farão parte da secretaria de determinado espaço.
 */
@Table(name = "secretaria_espaco")
@Entity(name = "SecretariaEspaco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SecretariaEspaco {
    
    /**
     * Identificador único da relação secretaria-espaço
     */
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    /**
     * Usuário que faz parte da secretaria do espaço
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_secretaria_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuarioSecretaria;

    /**
     * Espaço ao qual o usuário da secretaria está vinculado
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "espaco_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Espaco espaco;

    /**
     * Indica se o vínculo está ativo ou inativo
     */
    @Column(name = "esta_ativo", nullable = false)
    private Boolean estaAtivo = true;

    /**
     * Data e hora de criação do registro
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data e hora de inativação do registro
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Construtor para criar uma nova relação secretaria-espaço
     * 
     * @param usuarioSecretaria Usuário que fará parte da secretaria
     * @param espaco Espaço ao qual o usuário será vinculado
     */
    public SecretariaEspaco(Usuario usuarioSecretaria, Espaco espaco) {
        this.usuarioSecretaria = usuarioSecretaria;
        this.espaco = espaco;
        this.estaAtivo = true;
    }

    /**
     * Método executado antes de persistir a entidade no banco de dados.
     * Gera o ID único e define a data de criação.
     */
    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
        if (estaAtivo != null && !estaAtivo) {
            this.deletedAt = LocalDateTime.now();
        }
    }

    /**
     * Método executado antes de atualizar a entidade no banco de dados.
     * Gerencia automaticamente a data de inativação/reativação.
     */
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
