package br.uece.alunos.sisreserva.v1.domain.gestorEspaco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GestorEspacoRepository extends JpaRepository<GestorEspaco, String>, JpaSpecificationExecutor<GestorEspaco> {
    @Query("SELECT g FROM GestorEspaco g WHERE g.usuarioGestor.id = :usuarioId")
    List<GestorEspaco> findByUsuarioGestorId(String usuarioId);

    @Query("SELECT g FROM GestorEspaco g WHERE g.espaco.id = :espacoId")
    List<GestorEspaco> findByEspacoId(String espacoId);

    @Query("SELECT g FROM GestorEspaco g ORDER BY g.id ASC")
    Page<GestorEspaco> findAllOrderedById(Pageable pageable);

    @Query("""
        SELECT COUNT(g) > 0 FROM GestorEspaco g
        WHERE g.usuarioGestor.id = :usuarioId
          AND g.espaco.id = :espacoId
          AND g.estaAtivo = true
    """)
    boolean existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(String usuarioId, String espacoId);

    @Query("""
        SELECT g FROM GestorEspaco g 
        WHERE g.usuarioGestor.id = :usuarioId 
          AND g.espaco.id = :espacoId 
          AND g.estaAtivo = false
        """)
    Optional<GestorEspaco> findByUsuarioGestorIdAndEspacoIdAndEstaAtivoFalse(String usuarioId, String espacoId);
}
