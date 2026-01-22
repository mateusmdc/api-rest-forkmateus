package br.uece.alunos.sisreserva.v1.domain.gestorComplexoEspacos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade GestorComplexoEspacos.
 * Fornece métodos para consultar gestores de complexos de espaços.
 */
public interface GestorComplexoEspacosRepository extends JpaRepository<GestorComplexoEspacos, String>, JpaSpecificationExecutor<GestorComplexoEspacos> {
    
    @Query("SELECT g FROM GestorComplexoEspacos g WHERE g.usuarioGestor.id = :usuarioId")
    List<GestorComplexoEspacos> findByUsuarioGestorId(String usuarioId);

    @Query("SELECT g FROM GestorComplexoEspacos g WHERE g.complexoEspacos.id = :complexoEspacosId")
    List<GestorComplexoEspacos> findByComplexoEspacosId(String complexoEspacosId);

    @Query("""
        SELECT g FROM GestorComplexoEspacos g
        JOIN FETCH g.usuarioGestor
        WHERE g.complexoEspacos.id = :complexoEspacosId
          AND g.estaAtivo = true
    """)
    List<GestorComplexoEspacos> findGestoresAtivosComUsuarioByComplexoEspacosId(String complexoEspacosId);

    @Query("SELECT g FROM GestorComplexoEspacos g ORDER BY g.id ASC")
    Page<GestorComplexoEspacos> findAllOrderedById(Pageable pageable);

    @Query("""
        SELECT COUNT(g) > 0 FROM GestorComplexoEspacos g
        WHERE g.usuarioGestor.id = :usuarioId
          AND g.complexoEspacos.id = :complexoEspacosId
          AND g.estaAtivo = true
    """)
    boolean existsByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoTrue(String usuarioId, String complexoEspacosId);

    @Query("""
        SELECT g FROM GestorComplexoEspacos g 
        WHERE g.usuarioGestor.id = :usuarioId 
          AND g.complexoEspacos.id = :complexoEspacosId 
          AND g.estaAtivo = false
        """)
    Optional<GestorComplexoEspacos> findByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoFalse(String usuarioId, String complexoEspacosId);

    @Query("""
        SELECT g FROM GestorComplexoEspacos g 
        WHERE g.usuarioGestor.id = :usuarioId 
          AND g.complexoEspacos.id = :complexoEspacosId 
          AND g.estaAtivo = true
        """)
    Optional<GestorComplexoEspacos> findByUsuarioGestorIdAndComplexoEspacosIdAndEstaAtivoTrue(String usuarioId, String complexoEspacosId);
}
