package br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositório para acesso aos dados de equipamentos genéricos.
 * Extende JpaSpecificationExecutor para suportar consultas dinâmicas com Specifications.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Repository
public interface EquipamentoGenericoRepository extends JpaRepository<EquipamentoGenerico, String>,
        JpaSpecificationExecutor<EquipamentoGenerico> {
    
    /**
     * Verifica se existe um equipamento genérico com o nome especificado (case-insensitive e sem espaços).
     * 
     * @param nome Nome do equipamento genérico
     * @return true se existe, false caso contrário
     */
    @Query("SELECT COUNT(e) > 0 FROM EquipamentoGenerico e WHERE LOWER(TRIM(e.nome)) = LOWER(TRIM(:nome))")
    boolean existsByNomeIgnoreCaseAndTrimmed(String nome);
    
    /**
     * Verifica se existe um equipamento genérico com o nome especificado, excluindo um ID específico.
     * Útil para validação na atualização.
     * 
     * @param nome Nome do equipamento genérico
     * @param id ID do equipamento a ser excluído da busca
     * @return true se existe outro equipamento com o mesmo nome, false caso contrário
     */
    @Query("SELECT COUNT(e) > 0 FROM EquipamentoGenerico e WHERE LOWER(TRIM(e.nome)) = LOWER(TRIM(:nome)) AND e.id != :id")
    boolean existsByNomeIgnoreCaseAndTrimmedExcludingId(String nome, String id);
}
