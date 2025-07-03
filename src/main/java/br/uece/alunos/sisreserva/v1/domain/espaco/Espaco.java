package br.uece.alunos.sisreserva.v1.domain.espaco;

import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "espaco")
@Entity(name = "Espaco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Espaco {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @NotNull
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "url_cnpq", length = 255)
    private String urlCnpq;

    @Column(name = "observacao", length = 255)
    private String observacao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departamento_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Departamento departamento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "localizacao_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Localizacao localizacao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_espaco_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoEspaco tipoEspaco;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_atividade_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TipoAtividade tipoAtividade;

    @NotNull
    @Column(name = "precisa_projeto", nullable = false)
    private Boolean precisaProjeto = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
