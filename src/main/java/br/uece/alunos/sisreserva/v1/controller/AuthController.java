package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.AccessTokenDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/esqueci_senha")
    @Transactional
    public ResponseEntity<MessageResponseDTO> esqueciSenha(@RequestBody @Valid UsuarioEmailDTO data) {
        var messageResponseDTO = authService.esqueciMinhaSenha(data);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @PostMapping("/trocar_senha")
    @Transactional
    public ResponseEntity<MessageResponseDTO> resetPassword(@RequestBody @Valid UsuarioTrocarSenhaDTO data) {
        var messageResponseDTO = authService.resetarSenha(data);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<AccessTokenDTO> realizarLogin(@RequestBody @Valid UsuarioLoginDTO data, HttpServletResponse response, HttpServletRequest request) {
        AuthTokensDTO tokensJwt = authService.login(data, request);
        if (tokensJwt.refreshToken() != null) {
            cookieManager.addRefreshTokenCookie(response, tokensJwt.refreshToken());
        }
        AccessTokenDTO accessTokenDto = new AccessTokenDTO(tokensJwt.accessToken());
        return ResponseEntity.ok(accessTokenDto);
    }

    @GetMapping("/usuario/me")
    public ResponseEntity<UsuarioRetornoDTO> obterUsuarioPorTokenJWT(@RequestHeader("Authorization") String authorizationHeader) {
        var tokenJWT = authorizationHeader.substring(7);
        var usuario = authService.obterPorTokenJwt(tokenJWT);
        return ResponseEntity.ok(usuario);
    }
}
