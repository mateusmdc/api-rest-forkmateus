package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.usuario.AtualizarUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AtualizarUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityHandlerService entityHandlerService;

    @Autowired
    private UsuarioCargoService usuarioCargoService;

    @Autowired
    private TokenService tokenService;

    public UsuarioRetornoDTO atualizarUsuario(AtualizarUsuarioDTO data, String idUsuario) {
        var usuarioNoBanco = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ValidationException("Não foi encontrado nenhum usuário com o ID informado, durante o processo de atualização de um usuário."));

        Instituicao instituicao = data.instituicaoId() != null
                ? entityHandlerService.obterInstituicaoPorId(data.instituicaoId())
                : null;

        var usuarioCargosNoBanco = usuarioCargoService.obterCargosPorIdUsuario(usuarioNoBanco.getId());

        System.out.println("Usuários cargo no banco");
        System.out.println(usuarioCargosNoBanco);

        usuarioNoBanco.atualizarUsuario(data,instituicao);

        var usuarioAtualizado = usuarioRepository.save(usuarioNoBanco);

        if (!data.cargosId().isEmpty()) {
            atualizarCargos(data.cargosId());
        }

        return new UsuarioRetornoDTO(usuarioAtualizado);
    }
    //AQUI TEM A QUESTÃO DE ATUALIZAR (APAGAR E CRIAR) OS USUARIO_CARGO, ALÉM DOS DADOS NORMAIS

    public void atualizarCargos(List<String> cargosId) {

    }
}
