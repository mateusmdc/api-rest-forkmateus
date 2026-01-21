package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva;

import br.uece.alunos.sisreserva.v1.domain.equipamento.Equipamento;
import br.uece.alunos.sisreserva.v1.domain.espaco.Espaco;
import br.uece.alunos.sisreserva.v1.domain.projeto.Projeto;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa uma solicitação de reserva.
 * 
 * <p>Uma solicitação pode ser de espaço OU equipamento, nunca ambos simultaneamente.
 * Esta restrição é garantida por constraint no banco de dados.</p>
 * 
 * @author Sistema de Reservas - UECE
 * @version 2.0
 */
@Table(name = "solicitacao_reserva")
@Entity(name = "SolicitacaoReserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SolicitacaoReserva {
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @NotNull
    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    /**
     * Espaço sendo reservado.
     * Mutuamente exclusivo com equipamento (apenas um pode estar preenchido).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "espaco_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Espaco espaco;

    /**
     * Equipamento sendo reservado.
     * Mutuamente exclusivo com espaço (apenas um pode estar preenchido).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Equipamento equipamento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_solicitante_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuarioSolicitante;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusSolicitacao status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Projeto projeto;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_recorrencia", nullable = false)
    private TipoRecorrencia tipoRecorrencia = TipoRecorrencia.NAO_REPETE;

    @Column(name = "data_fim_recorrencia")
    private LocalDateTime dataFimRecorrencia;

    @Column(name = "reserva_pai_id", length = 36)
    private String reservaPaiId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica se esta reserva é de um espaço.
     * 
     * @return true se for reserva de espaço, false caso contrário
     */
    public boolean isReservaEspaco() {
        return this.espaco != null;
    }

    /**
     * Verifica se esta reserva é de um equipamento.
     * 
     * @return true se for reserva de equipamento, false caso contrário
     */
    public boolean isReservaEquipamento() {
        return this.equipamento != null;
    }
}
