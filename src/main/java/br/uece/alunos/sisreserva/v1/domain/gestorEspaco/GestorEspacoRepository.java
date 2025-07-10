package br.uece.alunos.sisreserva.v1.domain.gestorEspaco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GestorEspacoRepository extends JpaRepository<GestorEspaco, String> {
    @Query("SELECT g FROM GestorEspaco g WHERE g.usuarioGestor.id = :usuarioId")
    List<GestorEspaco> findByUsuarioGestorId(String usuarioId);

    @Query("SELECT g FROM GestorEspaco g WHERE g.espaco.id = :espacoId")
    List<GestorEspaco> findByEspacoId(String espacoId);

    @Query("SELECT g FROM GestorEspaco g ORDER BY g.id ASC")
    Page<GestorEspaco> findAllOrderedById(Pageable pageable);

    @Query("""
        SELECT COUNT(g) > 0 FROM GestorEspaco g 
        WHERE g.usuarioGestor.id = :usuarioId AND g.espaco.id = :espacoId
        """)
    boolean existsByUsuarioGestorIdAndEspacoId(String usuarioId, String espacoId);
}
