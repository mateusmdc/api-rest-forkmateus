package br.uece.alunos.sisreserva.v1.domain.auditLogLogin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogLoginRepository extends JpaRepository<AuditLogLogin, String> {
}
