package br.uece.alunos.api_aluga_espacos.v1.domain.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, String> {
    @Query("SELECT c FROM Curso c ORDER BY c.nome ASC")
    Page<Curso> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT c FROM Curso c WHERE LOWER(c.nome) = LOWER(:nome)")
    Curso findByNome(String nome);

    @Query("SELECT c FROM Curso c WHERE LOWER(TRIM(c.nome)) IN :nomes")
    List<Curso> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
}
