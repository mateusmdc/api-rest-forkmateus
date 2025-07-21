package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.utils.TokenDTO;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizaTokenAcesso {

    private final UsuarioRepository repository;
    private final TokenService tokenService;

    public TokenDTO atualizaToken(TokenDTO data) {
        var refreshToken = data.token();

        if (!tokenService.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido ou expirado.");
        }

        var email = tokenService.getSubject(refreshToken);

        var usuario = repository.findByEmailToHandle(email);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        if (!usuario.isEnabled()) {
            throw new RuntimeException("Usuário inativo.");
        }

        var novoAccessToken = tokenService.generateAccessToken(usuario);

        return new TokenDTO(novoAccessToken);
    }
}
