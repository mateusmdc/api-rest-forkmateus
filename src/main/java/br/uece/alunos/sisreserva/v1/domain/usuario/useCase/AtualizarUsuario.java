package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.AtualizarUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarUsuario {
    private final UsuarioRepository repository;
    private final UsuarioValidator validator;
    private final EntityHandlerService entityHandlerService;
    private final UsuarioCargoService usuarioCargoService;


    public UsuarioRetornoDTO atualizarUsuario(AtualizarUsuarioDTO data, String idUsuario) {
        validator.validarUsuarioId(idUsuario);

        var usuarioNoBanco = repository.findById(idUsuario)
                .orElseThrow(() -> new IllegalStateException("Usuário  não encontrado, mesmo após a validação do ID, durante o processo de atualização."));

        Instituicao instituicao = data.instituicaoId() != null
                ? entityHandlerService.obterInstituicaoPorId(data.instituicaoId())
                : null;

        usuarioNoBanco.atualizarUsuario(data,instituicao);

        var usuarioAtualizado = repository.save(usuarioNoBanco);

        if (!data.cargosId().isEmpty()) {
            usuarioCargoService.atualizarCargos(data.cargosId(), usuarioAtualizado.getId());
        }

        usuarioAtualizado = repository.findByIdToHandle(idUsuario);

        return new UsuarioRetornoDTO(usuarioAtualizado);
    }
}