package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioEmailDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioLoginDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AccessTokenDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.infra.utils.httpCookies.CookieManager;
import br.uece.alunos.sisreserva.v1.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Rotas de autenticação mapeadas no controller")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CookieManager cookieManager;

    @PostMapping("/criar")
    @Transactional
    public ResponseEntity<UsuarioRetornoDTO> criarUsuario(@RequestBody @Valid UsuarioDTO data) {
        var novoUsuarioDTO = authService.criarUsuario(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioDTO);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<AccessTokenDTO> performLogin(@RequestBody @Valid UsuarioLoginDTO data, HttpServletResponse response, HttpServletRequest request) {
        AuthTokensDTO tokensJwt = authService.signIn(data, request);
        if (tokensJwt.refreshToken() != null) {
            cookieManager.addRefreshTokenCookie(response, tokensJwt.refreshToken());
        }
        AccessTokenDTO accessTokenDto = new AccessTokenDTO(tokensJwt.accessToken());
        return ResponseEntity.ok(accessTokenDto);
    }

    @PostMapping("/esqueci_senha")
    @Transactional
    public ResponseEntity forgotPassword(@RequestBody UsuarioEmailDTO data) {
        var messageResponseDTO = authService.esqueciMinhaSenha(data);
        return ResponseEntity.ok(messageResponseDTO);
    }
}
