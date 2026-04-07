package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.credencialLocal.CredencialLocalRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@AllArgsConstructor
public class AtualizarUsuarioLoginErrado {

    private static final int MAX_ATTEMPTS = 5;

    private final UsuarioRepository usuarioRepository;
    private final CredencialLocalRepository credencialLocalRepository;
    private final TaskScheduler taskScheduler;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateFailedLogin(String email) {
        Usuario usuario = usuarioRepository.findByEmailToHandle(email);

        if (usuario == null) {
            throw new ValidationException("Não foi encontrado usuário com o email informado: " + email);
        }

        var credencialOptional = credencialLocalRepository.findByUsuarioId(usuario.getId());

        if (credencialOptional.isEmpty()) {
            return;
        }

        var credencial = credencialOptional.get();
        int failedAttempts = credencial.getAccessFailedCount() + 1;

        if (failedAttempts >= MAX_ATTEMPTS) {
            var lockoutEndTime = LocalDateTime.now().plusMinutes(15);
            credencial.setLockoutEnabled(true);
            credencial.setLockoutEnd(lockoutEndTime);
            String credencialId = credencial.getId();
            taskScheduler.schedule(() -> unlockUserAccount(credencialId),
                    lockoutEndTime.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            credencial.setAccessFailedCount(failedAttempts);
        }

        credencialLocalRepository.save(credencial);
    }

    @Transactional
    public void unlockUserAccount(String credencialId) {
        credencialLocalRepository.findById(credencialId).ifPresent(credencial -> {
            credencial.setLockoutEnabled(false);
            credencial.setAccessFailedCount(0);
            credencial.setLockoutEnd(null);
            credencialLocalRepository.save(credencial);
        });
    }
}