package br.uece.alunos.sisreserva.v1.domain.espaco.validation;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

/**
 * Componente responsável pelas validações de negócio relacionadas ao Espaco.
 * Centraliza as regras de validação para garantir consistência e segurança dos dados.
 */
@Component
public class EspacoValidator {

    @Autowired
    private EspacoRepository repository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public void validarSeEspacoJaExiste(String nome, String departamentoId, String localizacaoId) {
        String nomeNormalizado = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .trim()
                .toLowerCase();

        boolean jaExiste = repository.existsByNomeDepartamentoAndLocalizacao(
                nomeNormalizado, departamentoId, localizacaoId
        );

        if (jaExiste) {
            throw new IllegalArgumentException("Já existe um espaço com esse nome, departamento e localização.");
        }
    }
    /**
     * Valida se o usuário autenticado possui permissão para criar espaço.
     * Apenas administradores podem realizar esta operação.
     * 
     * @throws ValidationException se o usuário não for administrador
     */
    public void validarPermissaoParaCriar() {
        if (!usuarioAutenticadoService.isAdmin()) {
            throw new ValidationException(
                "Apenas administradores podem criar espaços."
            );
        }
    }

    /**
     * Valida se o usuário autenticado possui permissão para deletar espaço.
     * Apenas administradores podem realizar esta operação.
     * 
     * @throws ValidationException se o usuário não for administrador
     */
    public void validarPermissaoParaDeletar() {
        if (!usuarioAutenticadoService.isAdmin()) {
            throw new ValidationException(
                "Apenas administradores podem deletar espaços."
            );
        }
    }
    public void validarEspacoId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("O ID do espaço não pode ser nulo ou vazio.");
        }

        if (!repository.existsById(id)) {
            throw new ValidationException("Espaço com o ID fornecido não existe.");
        }
    }
}
