package br.uece.alunos.sisreserva.v1.domain.solicitacaoReserva;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa uma exceção pontual para uma ocorrência específica
 * de uma série de reservas recorrentes (novo modelo).
 *
 * No novo modelo de reservas recorrentes, apenas um único registro
 * é criado no banco para toda a série. Quando uma ocorrência específica precisa
 * ser cancelada, reagendada ou ter seu status alterado individualmente, um registro
 * de {@code ExcecaoRecorrencia} é criado aqui, sobrescrevendo somente aquela data.
 *
 * Ocorrências sem exceção herdam o status e os horários da série pai.
 *
 * @author Sistema de Reservas UECE
 * @version 1.0
 */
@Table(name = "excecao_recorrencia")
@Entity(name = "ExcecaoRecorrencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ExcecaoRecorrencia {

    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    /**
     * ID da série de reservas recorrentes a que esta exceção pertence.
     * Corresponde ao campo {@code id} da entidade {@link SolicitacaoReserva} com {@code isSerie = true}.
     */
    @NotNull
    @Column(name = "solicitacao_reserva_id", nullable = false, length = 36)
    private String solicitacaoReservaId;

    /**
     * Data da ocorrência original (conforme calculado pelo algoritmo de recorrência)
     * que esta exceção substitui.
     */
    @NotNull
    @Column(name = "data_ocorrencia", nullable = false)
    private LocalDate dataOcorrencia;

    /**
     * Novo horário de início para esta ocorrência específica.
     * {@code null} indica que o horário padrão da série deve ser mantido.
     */
    @Column(name = "data_inicio_nova")
    private LocalDateTime dataInicioNova;

    /**
     * Novo horário de fim para esta ocorrência específica.
     * {@code null} indica que a duração padrão da série deve ser mantida.
     */
    @Column(name = "data_fim_nova")
    private LocalDateTime dataFimNova;

    /**
     * Status efetivo desta ocorrência (ex.: CANCELADO, APROVADO, PENDENTE).
     * Sobrescreve o status herdado da série somente para esta data.
     */
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusSolicitacao status;

    /**
     * Motivo da exceção (cancelamento, reagendamento, etc.). Campo livre.
     */
    @Column(name = "motivo", length = 500)
    private String motivo;

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
}
