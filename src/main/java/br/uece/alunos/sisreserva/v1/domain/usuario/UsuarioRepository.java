package br.uece.alunos.sisreserva.v1.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String>, JpaSpecificationExecutor<Usuario> {
    UserDetails findByEmail(String email);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN true
            ELSE false END
            FROM Usuario u WHERE u.email = :email
            """)
    boolean usuarioExistsByEmail(String email);

    @Query("""
            SELECT u FROM Usuario u WHERE u.email = :email
            """)
    Usuario findByEmailToHandle(String email);

    @Query("""
            SELECT u FROM Usuario u WHERE u.id = :id
            """)
    Usuario findByIdToHandle(String id);

    @Query("SELECT u FROM Usuario u")
    Page<Usuario> findAllUsuariosPageable(Pageable pageable);


    @Query("""
    SELECT u FROM Usuario u
    JOIN UsuarioCargo uc ON uc.usuario = u
    JOIN Cargo c ON uc.cargo = c
    WHERE c.id = :cargoId
    """)
    Page<Usuario> findAllUsuariosByCargoId(String cargoId, Pageable pageable);
}
