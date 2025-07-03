package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoEquipamentoRepository extends JpaRepository<TipoEquipamento, String> {
    @Query("SELECT te FROM TipoEquipamento te ORDER BY te.nome ASC")
    Page<TipoEquipamento> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT te FROM TipoEquipamento te WHERE LOWER(te.nome) = LOWER(:nome)")
    TipoEquipamento findByNome(String nome);

    @Query("SELECT te FROM TipoEquipamento te WHERE LOWER(TRIM(te.nome)) IN :nomes")
    List<TipoEquipamento> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
}
