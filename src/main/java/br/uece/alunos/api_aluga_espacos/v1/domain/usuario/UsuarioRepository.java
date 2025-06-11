package br.uece.alunos.api_aluga_espacos.v1.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    UserDetails findByEmail(String email);
    UserDetails findByUsername(String username);

    @Query("""
            SELECT CASE WHEN COUNT(u) > 0 THEN true
            ELSE false END
            FROM User u WHERE u.email = :email
            """)
    boolean userExistsByEmail(String email);

    @Query("""
            SELECT u FROM User u WHERE u.email = :email
            """)
    Usuario findByEmailToHandle(String email);

    @Query("""
            SELECT u FROM User u WHERE u.id = :id
            """)
    Usuario findByIdToHandle(String id);
}
