package br.uece.alunos.sisreserva.v1.domain.credencialLdap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CredencialLdapRepository extends JpaRepository<CredencialLdap, String> {

    Optional<CredencialLdap> findByLdapUsername(String ldapUsername);

    Optional<CredencialLdap> findByUsuarioId(String usuarioId);

    /**
     * Loads the CredencialLdap with its Usuario and UsuarioCargos in a single query,
     * avoiding N+1 when generating tokens immediately after lookup.
     */
    @Query("""
        SELECT cl FROM CredencialLdap cl
        JOIN FETCH cl.usuario u
        JOIN FETCH u.usuarioCargos uc
        JOIN FETCH uc.cargo
        WHERE cl.ldapUsername = :ldapUsername
    """)
    Optional<CredencialLdap> findByLdapUsernameWithUsuario(@Param("ldapUsername") String ldapUsername);
}