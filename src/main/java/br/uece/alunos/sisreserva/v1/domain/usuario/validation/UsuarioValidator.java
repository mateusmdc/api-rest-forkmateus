package br.uece.alunos.sisreserva.v1.domain.usuario.validation;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioValidator {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validarEmailJaExistente(String email) {
        if (usuarioRepository.usuarioExistsByEmail(email)) {
            throw new ValidationException("Email já cadastrado no sistema.");
        }
    }

    public void validarEmailExistenteParaRecuperacao(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("O e-mail informado está vazio ou nulo.");
        }

        if (!usuarioRepository.usuarioExistsByEmail(email)) {
            throw new ValidationException("Não foi encontrado nenhum usuário com o e-mail informado para processo de Esqueci Minha Senha.");
        }
    }

    public void validarCredenciaisPreenchidas(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("O campo e-mail não pode estar vazio.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new ValidationException("O campo senha não pode estar vazio.");
        }
    }

    public void validarUsuarioId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("O ID do usuário não pode ser nulo ou vazio.");
        }

        if (!usuarioRepository.existsById(id)) {
            throw new ValidationException("Usuário com o ID fornecido não existe.");
        }
    }
}
