package br.uece.alunos.sisreserva.v1.domain.comite;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComiteRepository extends JpaRepository<Comite, String> {
    @Query("SELECT c FROM Comite c WHERE c.tipo = :tipo")
    List<Comite> findByTipo(TipoComite tipo);

    @Query("SELECT c FROM Comite c WHERE c.tipo IN :tipos")
    List<Comite> findByTipoIn(List<TipoComite> tipos);

    @NonNull
    @Query("SELECT c FROM Comite c ORDER BY c.id")
    Page<Comite> findAll(@NonNull Pageable pageable);
}
