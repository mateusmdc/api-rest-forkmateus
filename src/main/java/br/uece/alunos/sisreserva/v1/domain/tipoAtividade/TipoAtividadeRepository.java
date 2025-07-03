package br.uece.alunos.sisreserva.v1.domain.tipoAtividade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoAtividadeRepository extends JpaRepository<TipoAtividade, String> {
    @Query("SELECT ta FROM TipoAtividade ta ORDER BY ta.nome ASC")
    Page<TipoAtividade> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT ta FROM TipoAtividade ta WHERE LOWER(ta.nome) = LOWER(:nome)")
    TipoAtividade findByNome(String nome);

    @Query("SELECT ta FROM TipoAtividade ta WHERE LOWER(TRIM(ta.nome)) IN :nomes")
    List<TipoAtividade> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
}
