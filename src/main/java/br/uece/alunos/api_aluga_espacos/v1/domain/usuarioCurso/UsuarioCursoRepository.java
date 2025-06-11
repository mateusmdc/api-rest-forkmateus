package br.uece.alunos.api_aluga_espacos.v1.domain.usuarioCurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioCursoRepository extends JpaRepository<UsuarioCurso, String> {
    @Query("SELECT uc FROM UsuarioCurso uc WHERE uc.usuario.id = :usuarioId")
    UsuarioCurso findByUsuarioId(String usuarioId);

    @Query("SELECT uc FROM UsuarioCurso uc WHERE uc.curso.id = :cursoId")
    UsuarioCurso findByCursoId(String cursoId);
}
