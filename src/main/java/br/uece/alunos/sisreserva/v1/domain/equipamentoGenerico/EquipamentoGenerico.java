package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico;

import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa equipamentos genéricos do sistema.
 * Equipamentos genéricos são itens utilizados em grande quantidade em espaços,
 * como cadeiras, lixeiras, lousas, etc.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Entity
@Table(name = "equipamento_generico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EquipamentoGenerico {

    /**
     * Identificador único do equipamento genérico.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @Column(name = "id", nullable = false, length = 36, updatable = false)
    private String id;

    /**
     * Nome do equipamento genérico.
     * Exemplo: "Cadeira", "Lousa", "Lixeira", etc.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * Data e hora de criação do registro.
     * Preenchida automaticamente na criação.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data e hora da última atualização do registro.
     * Atualizada automaticamente a cada modificação.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Construtor para criação de equipamento genérico a partir de um DTO.
     * 
     * @param dto DTO contendo os dados do equipamento genérico
     */
    public EquipamentoGenerico(EquipamentoGenericoDTO dto) {
        this.nome = dto.nome();
    }

    /**
     * Atualiza os dados do equipamento genérico a partir de um DTO.
     * 
     * @param dto DTO contendo os novos dados
     */
    public void atualizar(EquipamentoGenericoDTO dto) {
        if (dto.nome() != null && !dto.nome().isBlank()) {
            this.nome = dto.nome();
        }
    }

    /**
     * Método executado antes da persistência do equipamento genérico.
     * Gera o ID único e define a data de criação.
     */
    @PrePersist
    public void onCreate() {
        this.id = UUID.randomUUID().toString().toUpperCase();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Método executado antes da atualização do equipamento genérico.
     * Atualiza a data de modificação.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
