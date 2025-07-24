package br.uece.alunos.sisreserva.v1.domain.departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, String>, JpaSpecificationExecutor<Departamento> {
    @Query("SELECT d FROM Departamento d ORDER BY d.nome ASC")
    Page<Departamento> findAllOrderedByNome(Pageable pageable);

    @Query("SELECT d FROM Departamento d WHERE LOWER(d.nome) = LOWER(:nome)")
    Departamento findByNome(String nome);

    @Query("SELECT d FROM Departamento d WHERE LOWER(TRIM(d.nome)) IN :nomes")
    List<Departamento> findAllByNomesIgnoreCaseAndTrimmed(List<String> nomes);
}
