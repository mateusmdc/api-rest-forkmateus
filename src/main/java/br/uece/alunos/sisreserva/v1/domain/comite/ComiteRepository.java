package br.uece.alunos.sisreserva.v1.domain.comite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComiteRepository extends JpaRepository<Comite, String>, JpaSpecificationExecutor<Comite> {
    @Query("SELECT c FROM Comite c WHERE c.tipo = :tipo")
    List<Comite> findByTipo(TipoComite tipo);

    @Query("SELECT c FROM Comite c WHERE c.tipo IN :tipos")
    List<Comite> findByTipoIn(List<TipoComite> tipos);

    @Query("SELECT c FROM Comite c ORDER BY c.id")
    Page<Comite> findAllPageable(Pageable pageable);
}
