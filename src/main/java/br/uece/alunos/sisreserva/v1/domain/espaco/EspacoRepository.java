package br.uece.alunos.sisreserva.v1.domain.espaco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EspacoRepository extends JpaRepository<Espaco, String> {
    @Query("SELECT e FROM Espaco e ORDER BY e.nome ASC")
    Page<Espaco> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT e FROM Espaco e WHERE LOWER(e.nome) = LOWER(:nome)")
    Espaco findByNome(String nome);

    @Query("SELECT e FROM Espaco e WHERE LOWER(TRIM(e.nome)) IN :nomes")
    List<Espaco> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);

    @Query("SELECT e FROM Espaco e WHERE e.tipoAtividade.id = :tipoAtividadeId")
    List<Espaco> findByTipoAtividade(String tipoAtividadeId);

    @Query("SELECT e FROM Espaco e WHERE e.tipoEspaco.id = :tipoEspacoId")
    List<Espaco> findByTipoEspaco(String tipoEspacoId);

    @Query("SELECT e FROM Espaco e WHERE e.departamento.id = :departamentoId")
    List<Espaco> findByDepartamento(String departamentoId);

    @Query("SELECT e FROM Espaco e WHERE e.localizacao.id = :localizacaoId")
    List<Espaco> findByLocalizacao(String localizacaoId);

    @Query("SELECT e FROM Espaco e WHERE e.precisaProjeto = true")
    List<Espaco> findAllQuePrecisamDeProjeto();

    @Query("SELECT e FROM Espaco e WHERE e.precisaProjeto = false")
    List<Espaco> findAllQueNaoPrecisamDeProjeto();
}