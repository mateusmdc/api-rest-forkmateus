package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.credencialLocal.CredencialLocalRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioTrocarSenhaDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TrocarSenha {
    private final UsuarioRepository repository;
    private final CredencialLocalRepository credencialLocalRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioValidator usuarioValidator;

    @Transactional
    public MessageResponseDTO resetarSenha(UsuarioTrocarSenhaDTO data) {
        try {
            usuarioValidator.validarEmailExistenteParaRecuperacao(data.email());

            var usuario = repository.findByEmailToHandle(data.email());

            var tokenMail = usuario.getTokenMail();
            var tokenExpiration = usuario.getTokenExpiration();
            var agora = LocalDateTime.now();

            var tokenIsValid = tokenMail != null && tokenMail.equals(data.tokenMail())
                    && tokenExpiration != null && agora.isBefore(tokenExpiration);

            if (tokenIsValid) {
                var credencial = credencialLocalRepository.findByUsuarioId(usuario.getId())
                        .orElseThrow(() -> new ValidationException("Credencial local não encontrada. Usuários institucionais não podem trocar a senha por este fluxo."));

                String encodedPassword = bCryptPasswordEncoder.encode(data.senha());
                credencial.setSenha(encodedPassword);
                usuario.setTokenMail(null);
                usuario.setTokenExpiration(null);

                return new MessageResponseDTO("Sucesso ao trocar a senha do Usuário.");
            } else {
                throw new ValidationException("Token de troca de senha inválido ou expirado.");
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException("Aconteceu um erro durante o processo de troca de senha: " + e.getMessage());
        }
    }
}