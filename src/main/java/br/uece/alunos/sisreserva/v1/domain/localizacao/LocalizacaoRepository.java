package br.uece.alunos.sisreserva.v1.domain.localizacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, String> {
    @Query("SELECT l FROM Localizacao l ORDER BY l.nome ASC")
    Page<Localizacao> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT l FROM Localizacao l WHERE LOWER(l.nome) = LOWER(:nome)")
    Localizacao findByNome(String nome);

    @Query("SELECT l FROM Localizacao l WHERE LOWER(TRIM(l.nome)) IN :nomes")
    List<Localizacao> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
}
