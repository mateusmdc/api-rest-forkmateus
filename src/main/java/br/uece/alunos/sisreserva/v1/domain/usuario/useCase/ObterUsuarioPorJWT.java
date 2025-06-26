package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObterUsuarioPorJWT {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private TokenService tokenService;

    public UsuarioRetornoDTO obterPorTokenJwt(String tokenJWT) {
        var usuarioId = tokenService.getIdClaim(tokenJWT);
        usuarioId = usuarioId.replaceAll("\"", "");

        var usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new ValidationException("Não foi encontrado Usuário com o ID do token de autenticação."));

        return new UsuarioRetornoDTO(usuario);
    }
}
