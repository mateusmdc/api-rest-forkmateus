package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.utils.TokenDTO;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import br.uece.alunos.sisreserva.v1.service.RefreshTokenLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizaTokenAcesso {
    private final UsuarioRepository repository;
    private final TokenService tokenService;
    private final RefreshTokenLogService refreshTokenLogService;

    public TokenDTO atualizaToken(String refreshToken) {
        if (!tokenService.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido ou expirado.");
        }

        var decoded = tokenService.parseClaims(refreshToken);
        var refreshId = decoded.getClaim("refreshId").asString();

        boolean revoked = refreshTokenLogService.foiRevogado(refreshId);
        if (revoked) {
            throw new RuntimeException("Refresh token foi revogado.");
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
