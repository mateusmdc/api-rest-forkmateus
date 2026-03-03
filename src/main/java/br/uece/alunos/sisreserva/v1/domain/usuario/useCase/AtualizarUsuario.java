package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.AtualizarUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por atualizar dados de um usuário existente.
 * Permite atualização parcial de todos os campos, incluindo senha.
 */
@Component
@AllArgsConstructor
public class AtualizarUsuario {
    private final UsuarioRepository repository;
    private final UsuarioValidator validator;
    private final EntityHandlerService entityHandlerService;
    private final UsuarioCargoService usuarioCargoService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Atualiza os dados de um usuário existente.
     * Todos os campos são opcionais - apenas campos fornecidos serão atualizados.
     * Se uma nova senha for fornecida, ela será criptografada antes de ser salva.
     *
     * @param data Dados a serem atualizados
     * @param idUsuario ID do usuário a ser atualizado
     * @return DTO com os dados atualizados do usuário
     * @throws IllegalStateException se o usuário não for encontrado
     */
    public UsuarioRetornoDTO atualizarUsuario(AtualizarUsuarioDTO data, String idUsuario) {
        // Valida se o ID do usuário é válido e se o usuário existe
        validator.validarUsuarioId(idUsuario);

        var usuarioNoBanco = repository.findById(idUsuario)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado, mesmo após a validação do ID, durante o processo de atualização."));

        // Busca a instituição se um novo ID de instituição foi fornecido
        Instituicao instituicao = data.instituicaoId() != null
                ? entityHandlerService.obterInstituicaoPorId(data.instituicaoId())
                : null;

        // Criptografa a senha se uma nova senha foi fornecida
        String senhaEncriptada = null;
        if (data.senha() != null && !data.senha().isBlank()) {
            senhaEncriptada = bCryptPasswordEncoder.encode(data.senha());
        }

        // Atualiza os dados do usuário
        usuarioNoBanco.atualizarUsuario(data, instituicao, senhaEncriptada);

        var usuarioAtualizado = repository.save(usuarioNoBanco);

        // Atualiza os cargos se novos cargos foram fornecidos
        if (data.cargosId() != null && !data.cargosId().isEmpty()) {
            usuarioCargoService.atualizarCargos(data.cargosId(), usuarioAtualizado.getId());
        }

        // Busca o usuário atualizado com todos os relacionamentos carregados
        usuarioAtualizado = repository.findByIdToHandle(idUsuario);

        return new UsuarioRetornoDTO(usuarioAtualizado);
    }
}