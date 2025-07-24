package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioTrocarSenhaDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TrocarSenha {
    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioValidator usuarioValidator;

    public MessageResponseDTO resetarSenha(UsuarioTrocarSenhaDTO data) {
        try {
            usuarioValidator.validarEmailExistenteParaRecuperacao(data.email());

            var usuario = repository.findByEmailToHandle(data.email());

            var tokenMail = usuario.getTokenMail();
            var tokenExpiration = usuario.getTokenExpiration();
            var agora = LocalDateTime.now();

            var tokenIsValid = tokenMail.equals(data.tokenMail()) && agora.isBefore(tokenExpiration);

            if (tokenIsValid) {
                String encodedPassword = bCryptPasswordEncoder.encode(data.senha());
                usuario.setSenha(encodedPassword);
                return new MessageResponseDTO("Sucesso ao trocar a senha do Usuário.");
            } else {
                throw new ValidationException("Token de troca de senha inválido.");
            }
        }
        catch (Exception e) {
            throw new ValidationException("Aconteceu um erro durante o processo de troca de senha: " + e.getMessage());
        }
    }
}