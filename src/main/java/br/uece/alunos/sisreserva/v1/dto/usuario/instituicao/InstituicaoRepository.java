package br.uece.alunos.sisreserva.v1.dto.usuario.instituicao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InstituicaoRepository extends JpaRepository<Instituicao, String> {
    @Query("SELECT i FROM Instituicao i ORDER BY i.nome ASC")
    Page<Instituicao> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT i FROM Instituicao i WHERE LOWER(i.nome) = LOWER(:nome)")
    Instituicao findByNome(String nome);

    @Query("SELECT i FROM Instituicao i WHERE LOWER(TRIM(i.nome)) IN :nomes")
    List<Instituicao> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);

    @Query("SELECT i FROM Instituicao i WHERE i.id IN :ids")
    List<Instituicao> findAllById(List<String> ids);

}
