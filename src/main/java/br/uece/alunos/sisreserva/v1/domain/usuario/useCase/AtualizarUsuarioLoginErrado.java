package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.auditLogLogin.useCase.RegisterAuditLog;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class AtualizarUsuarioLoginErrado {

    private static final int MAX_ATTEMPTS = 5;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFailedLogin(String email) {
        Usuario usuario = usuarioRepository.findByEmailToHandle(email);

        if (usuario == null) {
            throw new ValidationException("Não foi encontrado usuário com o email informado: " + email);
        }

        int failedAttempts = usuario.getAccessFailedCount() + 1;

        if (failedAttempts >= MAX_ATTEMPTS) {
            var lockoutEndTime = LocalDateTime.now().plusMinutes(15);
            usuario.setLockoutEnabled(true);
            usuario.setLockoutEnd(lockoutEndTime);
            taskScheduler.schedule(() -> unlockUserAccount(usuario),
                    lockoutEndTime.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            usuario.setAccessFailedCount(failedAttempts);
        }

        usuarioRepository.save(usuario);
    }

    private void unlockUserAccount(Usuario usuario) {
        usuario.setLockoutEnabled(false);
        usuario.setAccessFailedCount(0);
        usuario.setLockoutEnd(null);
        usuarioRepository.save(usuario);
    }
}
