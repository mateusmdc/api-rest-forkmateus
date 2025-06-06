package br.uece.alunos.api_aluga_espacos.v1.domain.cargo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, String> {
    @Query("SELECT c FROM Cargo c ORDER BY c.nome ASC")
    Page<Cargo> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT c FROM Cargo c WHERE LOWER(c.nome) = LOWER(:nome)")
    Cargo findByNome(String nome);

    @Query("SELECT c FROM Cargo c WHERE LOWER(TRIM(c.nome)) IN :nomes")
    List<Cargo> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
}
