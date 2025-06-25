package br.uece.alunos.sisreserva.v1.domain.auditLogLogin;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "audit_log_login")
@Entity(name = "AuditLogLogin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AuditLogLogin {
    @Id
    @Column(name = "id", nullable = false, length=36, updatable=false)
    private String id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "host_name", length = 255)
    private String hostName;

    @Column(name = "server_name", length = 255)
    private String serverName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
