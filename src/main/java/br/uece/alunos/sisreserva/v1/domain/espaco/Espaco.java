package br.uece.alunos.sisreserva.v1.domain.espaco;

import br.uece.alunos.sisreserva.v1.domain.complexoEspacos.ComplexoEspacos;
import br.uece.alunos.sisreserva.v1.domain.departamento.Departamento;
import br.uece.alunos.sisreserva.v1.domain.localizacao.Localizacao;
import br.uece.alunos.sisreserva.v1.domain.tipoAtividade.TipoAtividade;
import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.TipoEspaco;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Relacionamento ManyToMany com TipoAtividade.
     * Um espaço pode ter múltiplos tipos de atividade e um tipo de atividade pode estar em múltiplos espaços.
     * O lado proprietário da relação é Espaco (possui @JoinTable).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "espaco_tipo_atividade",
        joinColumns = @JoinColumn(name = "espaco_id"),
        inverseJoinColumns = @JoinColumn(name = "tipo_atividade_id")
    )
    private List<TipoAtividade> tiposAtividade = new ArrayList<>();

    @NotNull
    @Column(name = "precisa_projeto", nullable = false)
    private Boolean precisaProjeto = false;

    @NotNull
    @Column(name = "multiusuario", nullable = false)
    private Boolean multiusuario = false;

    /**
     * Indica se o espaço está disponível para reserva.
     * Default: true (disponível)
     */
    @NotNull
    @Column(name = "reservavel", nullable = false)
    private Boolean reservavel = true;

    @ManyToMany(mappedBy = "espacos", fetch = FetchType.LAZY)
    private List<ComplexoEspacos> complexos = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    public void atualizar(EspacoAtualizarDTO data) {
        if (data.nome() != null && !data.nome().isEmpty()) {
            this.nome = data.nome();
        }
        if (data.urlCnpq() != null) {
            this.urlCnpq = data.urlCnpq();
        }
        if (data.observacao() != null) {
            this.observacao = data.observacao();
        }
        if (data.precisaProjeto() != null) {
            this.precisaProjeto = data.precisaProjeto();
        }
        if (data.multiusuario() != null) {
            this.multiusuario = data.multiusuario();
        }
        if (data.reservavel() != null) {
            this.reservavel = data.reservavel();
        }
    }

    /**
     * Atualiza os tipos de atividade do espaço.
     * Remove todos os tipos de atividade existentes e adiciona os novos.
     * 
     * @param novosTiposAtividade nova lista de tipos de atividade
     */
    public void atualizarTiposAtividade(List<TipoAtividade> novosTiposAtividade) {
        this.tiposAtividade.clear();
        if (novosTiposAtividade != null && !novosTiposAtividade.isEmpty()) {
            this.tiposAtividade.addAll(novosTiposAtividade);
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
