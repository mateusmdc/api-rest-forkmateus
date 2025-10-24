package br.uece.alunos.sisreserva.v1.domain.tipoEspaco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TipoEspacoRepository extends JpaRepository<TipoEspaco, String>, JpaSpecificationExecutor<TipoEspaco> {
    @Query("SELECT te FROM TipoEspaco te ORDER BY te.nome ASC")
    Page<TipoEspaco> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT te FROM TipoEspaco te WHERE LOWER(te.nome) = LOWER(:nome)")
    TipoEspaco findByNome(String nome);

    @Query("SELECT te FROM TipoEspaco te WHERE LOWER(TRIM(te.nome)) IN :nomes")
    List<TipoEspaco> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
    
    @Query("SELECT te FROM TipoEspaco te WHERE LOWER(te.nome) = LOWER(:nome)")
    Optional<TipoEspaco> findByNomeIgnoreCase(String nome);
}
