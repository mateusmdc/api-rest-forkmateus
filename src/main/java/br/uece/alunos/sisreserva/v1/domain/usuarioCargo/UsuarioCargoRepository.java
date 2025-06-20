package br.uece.alunos.sisreserva.v1.domain.usuarioCargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioCargoRepository extends JpaRepository<UsuarioCargo, String> {
    @Query("SELECT uc FROM UsuarioCargo uc WHERE uc.usuario.id = :usuarioId")
    UsuarioCargo findByUsuarioId(String usuarioId);

    @Query("SELECT uc FROM UsuarioCargo uc WHERE uc.cargo.id = :cargoId")
    UsuarioCargo findByCargoId(String cargoId);

    @Query("""
            SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END
            FROM UsuarioCargo uc
            WHERE uc.usuario.id = :usuarioId AND uc.cargo.id = :cargoId
    """)
    boolean existsByUsuarioIdAndCargoId(String usuarioId, String cargoId);
}
