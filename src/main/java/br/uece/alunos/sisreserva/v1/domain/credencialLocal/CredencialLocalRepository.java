package br.uece.alunos.sisreserva.v1.domain.credencialLocal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CredencialLocalRepository extends JpaRepository<CredencialLocal, String> {
    Optional<CredencialLocal> findByUsuarioId(String usuarioId);
}