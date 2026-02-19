package br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade EquipamentoGenericoEspaco.
 * Fornece operações de consulta e manipulação de vínculos entre equipamentos genéricos e espaços.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
public interface EquipamentoGenericoEspacoRepository extends JpaRepository<EquipamentoGenericoEspaco, String>, 
                                                              JpaSpecificationExecutor<EquipamentoGenericoEspaco> {

    /**
     * Busca todos os equipamentos genéricos vinculados a um espaço específico.
     * 
     * @param espacoId ID do espaço
     * @return Lista de equipamentos genéricos do espaço
     */
    @Query("""
        SELECT ege FROM EquipamentoGenericoEspaco ege
        JOIN FETCH ege.equipamentoGenerico eg
        WHERE ege.espaco.id = :espacoId
        ORDER BY eg.nome ASC
    """)
    List<EquipamentoGenericoEspaco> findByEspacoIdWithEquipamento(String espacoId);

    /**
     * Busca todos os espaços que possuem um equipamento genérico específico.
     * 
     * @param equipamentoGenericoId ID do equipamento genérico
     * @return Lista de espaços que possuem o equipamento genérico
     */
    @Query("""
        SELECT ege FROM EquipamentoGenericoEspaco ege
        JOIN FETCH ege.espaco e
        WHERE ege.equipamentoGenerico.id = :equipamentoGenericoId
        ORDER BY e.nome ASC
    """)
    List<EquipamentoGenericoEspaco> findByEquipamentoGenericoIdWithEspaco(String equipamentoGenericoId);

    /**
     * Verifica se já existe um vínculo entre o equipamento genérico e o espaço.
     * 
     * @param equipamentoGenericoId ID do equipamento genérico
     * @param espacoId ID do espaço
     * @return true se o vínculo existe, false caso contrário
     */
    @Query("""
        SELECT COUNT(ege) > 0 FROM EquipamentoGenericoEspaco ege
        WHERE ege.equipamentoGenerico.id = :equipamentoGenericoId
          AND ege.espaco.id = :espacoId
    """)
    boolean existsByEquipamentoGenericoIdAndEspacoId(String equipamentoGenericoId, String espacoId);

    /**
     * Busca um vínculo específico entre equipamento genérico e espaço.
     * 
     * @param equipamentoGenericoId ID do equipamento genérico
     * @param espacoId ID do espaço
     * @return Optional contendo o vínculo se existir
     */
    @Query("""
        SELECT ege FROM EquipamentoGenericoEspaco ege
        WHERE ege.equipamentoGenerico.id = :equipamentoGenericoId
          AND ege.espaco.id = :espacoId
    """)
    Optional<EquipamentoGenericoEspaco> findByEquipamentoGenericoIdAndEspacoId(
        String equipamentoGenericoId, 
        String espacoId
    );
}
