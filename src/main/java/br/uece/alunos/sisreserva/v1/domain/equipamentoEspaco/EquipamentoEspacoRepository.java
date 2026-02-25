package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EquipamentoEspacoRepository extends JpaRepository<EquipamentoEspaco, String>, JpaSpecificationExecutor<EquipamentoEspaco> {
    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.equipamento.id = :equipamentoId")
    List<EquipamentoEspaco> findByEquipamentoId(String equipamentoId);

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.espaco.id = :espacoId")
    List<EquipamentoEspaco> findAllByEspacoId(String espacoId);

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.espaco.id = :espacoId AND ee.dataRemocao IS NULL")
    List<EquipamentoEspaco> findAlocadosByEspacoId(String espacoId);

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.dataRemocao IS NULL")
    List<EquipamentoEspaco> findAllAlocados();

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.equipamento.id = :equipamentoId AND ee.dataRemocao IS NULL")
    List<EquipamentoEspaco> findAlocadosByEquipamentoId(String equipamentoId);

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.espaco.id = :espacoId AND ee.dataRemocao IS NOT NULL")
    List<EquipamentoEspaco> findRemovidosByEspacoId(String espacoId);

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.espaco.id = :espacoId AND ee.dataRemocao BETWEEN :startDate AND :endDate")
    List<EquipamentoEspaco> findRemovidosByEspacoAndPeriodo(String espacoId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.espaco.id = :espacoId AND ee.dataAlocacao BETWEEN :startDate AND :endDate")
    List<EquipamentoEspaco> findAdicionadosByEspacoAndPeriodo(String espacoId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca vinculação ativa de equipamento por ID do equipamento.
     * Retorna apenas equipamentos que não foram removidos (dataRemocao = null).
     * 
     * @param equipamentoId ID do equipamento
     * @return lista de vinculações ativas (geralmente apenas uma)
     */
    @Query("SELECT ee FROM EquipamentoEspaco ee WHERE ee.equipamento.id = :equipamentoId AND ee.dataRemocao IS NULL")
    List<EquipamentoEspaco> findByEquipamentoIdAndDataRemocaoIsNull(String equipamentoId);

    /**
     * Busca os IDs dos equipamentos ativos vinculados a uma lista de espaços.
     * Útil para filtrar reservas de equipamentos que gestores/secretarias podem visualizar.
     * 
     * @param espacosIds Lista de IDs dos espaços
     * @return Lista com os IDs dos equipamentos vinculados aos espaços
     */
    @Query("""
        SELECT ee.equipamento.id FROM EquipamentoEspaco ee
        WHERE ee.espaco.id IN :espacosIds
          AND ee.dataRemocao IS NULL
    """)
    List<String> findEquipamentosIdsVinculadosAosEspacos(List<String> espacosIds);
    
    /**
     * Busca os IDs dos equipamentos ativos vinculados a um espaço específico.
     * 
     * @param espacoId ID do espaço
     * @return Lista com os IDs dos equipamentos vinculados ao espaço
     */
    @Query("SELECT ee.equipamento.id FROM EquipamentoEspaco ee WHERE ee.espaco.id = :espacoId AND ee.dataRemocao IS NULL")
    List<String> findEquipamentosIdsByEspacoId(String espacoId);
}