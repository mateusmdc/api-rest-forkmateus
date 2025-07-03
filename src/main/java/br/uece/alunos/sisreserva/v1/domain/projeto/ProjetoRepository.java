package br.uece.alunos.sisreserva.v1.domain.projeto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, String> {

    @Query("SELECT p FROM Projeto p WHERE p.usuarioResponsavel.id = :usuarioId")
    List<Projeto> findByUsuarioResponsavelId(String usuarioId);

    @Query("SELECT p FROM Projeto p WHERE p.instituicao.id = :instituicaoId")
    List<Projeto> findByInstituicaoId(String instituicaoId);

    @Query("SELECT p FROM Projeto p WHERE p.dataInicio >= :dataInicio AND p.dataFim <= :dataFim")
    List<Projeto> findByPeriodo(LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT p FROM Projeto p ORDER BY p.createdAt DESC")
    Page<Projeto> findAllPageable(Pageable pageable);
}
