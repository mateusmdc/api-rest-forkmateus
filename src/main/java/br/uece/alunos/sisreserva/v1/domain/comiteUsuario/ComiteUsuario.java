package br.uece.alunos.sisreserva.v1.domain.comiteUsuario;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "comite_usuario")
@Entity(name = "ComiteUsuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ComiteUsuario {
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comite_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comite comite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Departamento departamento;

    @Column(name = "descricao", length = 100)
    @Size(max = 100)
    private String descricao;

    @Column(name = "portaria", length = 50)
    @Size(max = 50)
    private String portaria;

    @Column(name = "is_titular", nullable = false)
    private Boolean isTitular = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ComiteUsuario(ComiteUsuarioDTO data, Comite c, Usuario u, Departamento d) {
        this.usuario = u;
        this.comite = c;
        this.departamento = d;
        this.descricao = data.descricao();
        this.portaria = data.portaria();
        this.isTitular = data.isTitular();
    }

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    public void atualizar(ComiteUsuarioAtualizarDTO data, Departamento departamento) {
        if (data.descricao() != null) {
            this.descricao = data.descricao();
        }

        if (data.isTitular() != null) {
            this.isTitular = data.isTitular();
        }

        if (data.departamentoId() != null && departamento != null) {
            this.departamento = departamento;
        }
    }


    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
