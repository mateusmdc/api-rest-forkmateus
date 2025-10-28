package br.uece.alunos.sisreserva.v1.domain.complexoEspacos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComplexoEspacosRepository extends JpaRepository<ComplexoEspacos, String>, JpaSpecificationExecutor<ComplexoEspacos> {
    @Query("SELECT c FROM ComplexoEspacos c WHERE LOWER(c.nome) = LOWER(:nome)")
    Optional<ComplexoEspacos> findByNomeIgnoreCase(String nome);
}
