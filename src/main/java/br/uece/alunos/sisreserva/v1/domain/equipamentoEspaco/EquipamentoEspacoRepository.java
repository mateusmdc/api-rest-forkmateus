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
}