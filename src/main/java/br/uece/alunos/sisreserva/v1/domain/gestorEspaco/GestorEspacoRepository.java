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

    @Query("""
        SELECT g FROM GestorEspaco g
        JOIN FETCH g.usuarioGestor
        WHERE g.espaco.id = :espacoId
          AND g.estaAtivo = true
    """)
    List<GestorEspaco> findGestoresAtivosComUsuarioByEspacoId(String espacoId);

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

    @Query("""
        SELECT g FROM GestorEspaco g 
        WHERE g.usuarioGestor.id = :usuarioId 
          AND g.espaco.id = :espacoId 
          AND g.estaAtivo = true
        """)
    Optional<GestorEspaco> findByUsuarioGestorIdAndEspacoIdAndEstaAtivo(String usuarioId, String espacoId);

    /**
     * Busca os IDs dos espaços que o usuário gerencia ativamente.
     * Útil para filtrar reservas que o gestor pode visualizar.
     * 
     * @param usuarioId ID do usuário gestor
     * @return Lista com os IDs dos espaços gerenciados
     */
    @Query("""
        SELECT g.espaco.id FROM GestorEspaco g
        WHERE g.usuarioGestor.id = :usuarioId
          AND g.estaAtivo = true
    """)
    List<String> findEspacosIdsGerenciadosByUsuarioId(String usuarioId);
}
