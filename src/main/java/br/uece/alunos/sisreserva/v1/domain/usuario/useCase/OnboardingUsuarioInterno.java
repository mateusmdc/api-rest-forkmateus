package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.credencialLdap.CredencialLdap;
import br.uece.alunos.sisreserva.v1.domain.credencialLdap.CredencialLdapRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.OnboardingUsuarioInternoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.security.TokenService;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.RefreshTokenLogService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.DocumentoFiscalUtils;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.TelefoneUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

/**
 * Completes the registration of an internal user after their first successful LDAP login.
 *
 * The onboarding token proves that LDAP authentication already succeeded — without it,
 * no one can self-register as a USUARIO_INTERNO through this endpoint.
 *
 * On completion the user is immediately authenticated: auth tokens are returned
 * so the client does not need to perform a second login.
 */
@Component
@AllArgsConstructor
public class OnboardingUsuarioInterno {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final CredencialLdapRepository credencialLdapRepository;
    private final EntityHandlerService entityHandlerService;
    private final UsuarioValidator usuarioValidator;
    private final UsuarioCargoService usuarioCargoService;
    private final RefreshTokenLogService refreshTokenLogService;

    @Transactional
    public AuthTokensDTO completarOnboarding(OnboardingUsuarioInternoDTO data, HttpServletRequest request) {
        if (!tokenService.isOnboardingTokenValid(data.onboardingToken())) {
            throw new ValidationException("Token de onboarding inválido ou expirado.");
        }

        String ldapUsername = tokenService.getLdapUsernameFromOnboardingToken(data.onboardingToken());

        if (credencialLdapRepository.findByLdapUsername(ldapUsername).isPresent()) {
            throw new ValidationException("Este usuário institucional já completou o onboarding.");
        }

        usuarioValidator.validarEmailJaExistente(data.email());

        String cpfNormalizado = null;
        if (data.documentoFiscal() != null && !data.documentoFiscal().isBlank()) {
            cpfNormalizado = DocumentoFiscalUtils.normalizarCPF(data.documentoFiscal());
            usuarioValidator.validarCPF(cpfNormalizado);
            usuarioValidator.validarCPFJaExistente(cpfNormalizado);
        }

        String telefoneNormalizado = null;
        if (data.telefone() != null && !data.telefone().isBlank()) {
            telefoneNormalizado = TelefoneUtils.normalizarTelefone(data.telefone());
            usuarioValidator.validarTelefone(telefoneNormalizado);
        }

        var instituicao = entityHandlerService.obterInstituicaoPorId(data.instituicaoId());

        var novoUsuario = new Usuario();
        novoUsuario.setNome(data.nome());
        novoUsuario.setEmail(data.email());
        novoUsuario.setDocumentoFiscal(cpfNormalizado);
        novoUsuario.setTelefone(telefoneNormalizado);
        novoUsuario.setInstituicao(instituicao);
        novoUsuario.setRefreshTokenEnabled(false);

        var usuarioSalvo = usuarioRepository.save(novoUsuario);

        credencialLdapRepository.save(new CredencialLdap(usuarioSalvo, ldapUsername));

        var cargaDTO = new CriarCargaUsuarioCargoDTO(usuarioSalvo.getId(), List.of("USUARIO_INTERNO"));
        usuarioCargoService.criarEmCargaUsuarioCargo(cargaDTO);

        var usuarioComCargos = usuarioRepository.findByIdToHandle(usuarioSalvo.getId());

        var accessToken  = tokenService.generateAccessToken(usuarioComCargos);
        var refreshToken = tokenService.generateRefreshToken(usuarioComCargos);

        var refreshClaims  = tokenService.parseClaims(refreshToken);
        var refreshTokenId = refreshClaims.getClaim("refreshId").asString();
        var issuedAt       = refreshClaims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        refreshTokenLogService.registrar(usuarioComCargos, refreshTokenId, issuedAt, null, request);

        return new AuthTokensDTO(accessToken, refreshToken);
    }
}