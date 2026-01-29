package br.uece.alunos.sisreserva.v1.domain.usuario.validation;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.DocumentoFiscalUtils;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.TelefoneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Componente responsável pelas validações de negócio relacionadas ao Usuário.
 * Centraliza as regras de validação para garantir consistência dos dados.
 */
@Component
public class UsuarioValidator {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Valida se o e-mail já está cadastrado no sistema.
     * 
     * @param email E-mail a ser validado
     * @throws ValidationException se o e-mail já existir
     */
    public void validarEmailJaExistente(String email) {
        if (usuarioRepository.usuarioExistsByEmail(email)) {
            throw new ValidationException("Email já cadastrado no sistema.");
        }
    }

    /**
     * Valida se o CPF já está cadastrado no sistema.
     * 
     * @param cpf CPF normalizado (apenas dígitos) a ser validado
     * @throws ValidationException se o CPF já existir
     */
    public void validarCPFJaExistente(String cpf) {
        if (usuarioRepository.usuarioExistsByDocumentoFiscal(cpf)) {
            throw new ValidationException("CPF já cadastrado no sistema.");
        }
    }

    /**
     * Valida se o CPF é válido segundo o algoritmo oficial.
     * 
     * @param cpf CPF a ser validado (pode conter formatação)
     * @throws ValidationException se o CPF for inválido
     */
    public void validarCPF(String cpf) {
        if (!DocumentoFiscalUtils.validarCPF(cpf)) {
            throw new ValidationException("CPF inválido.");
        }
    }

    /**
     * Valida se o telefone tem o tamanho correto (10 ou 11 dígitos).
     * 
     * @param telefone Telefone a ser validado (pode conter formatação)
     * @throws ValidationException se o telefone for inválido
     */
    public void validarTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty() && !TelefoneUtils.validarTelefone(telefone)) {
            throw new ValidationException("Telefone inválido. Deve conter de 10 a 11 dígitos.");
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
