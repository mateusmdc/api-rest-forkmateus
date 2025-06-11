package br.uece.alunos.api_aluga_espacos.v1.domain.usuarioCargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioCargoRepository extends JpaRepository<UsuarioCargo, String> {
    @Query("SELECT uc FROM UsuarioCargo uc WHERE uc.usuario.id = :usuarioId")
    UsuarioCargo findByUsuarioId(String usuarioId);

    @Query("SELECT uc FROM UsuarioCargo uc WHERE uc.cargo.id = :cargoId")
    UsuarioCargo findByCargoId(String cargoId);
}
