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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<UsuarioRetornoDTO> criarUsuario(@RequestBody @Valid UsuarioDTO data) {
        var novoUsuarioDTO = authService.criarUsuario(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuarioDTO);
    }

    @PostMapping("/password/forgot")
    @Transactional
    public ResponseEntity<MessageResponseDTO> esqueciSenha(@RequestBody @Valid UsuarioEmailDTO data) {
        var messageResponseDTO = authService.esqueciMinhaSenha(data);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @PostMapping("/password/reset")
    @Transactional
    public ResponseEntity<MessageResponseDTO> resetPassword(@RequestBody @Valid UsuarioTrocarSenhaDTO data) {
        var messageResponseDTO = authService.resetarSenha(data);
        return ResponseEntity.ok(messageResponseDTO);
    }

    @PostMapping("/signin")
    @Transactional
    public ResponseEntity<AccessTokenDTO> realizarLogin(@RequestBody @Valid UsuarioLoginDTO data, HttpServletResponse response, HttpServletRequest request) {
        AuthTokensDTO tokensJwt = authService.login(data, request);
        if (tokensJwt.refreshToken() != null) {
            cookieManager.addRefreshTokenCookie(response, tokensJwt.refreshToken());
        }
        AccessTokenDTO accessTokenDto = new AccessTokenDTO(tokensJwt.accessToken());
        return ResponseEntity.ok(accessTokenDto);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UsuarioRetornoDTO> atualizarUsuario(@PathVariable String userId, @RequestBody AtualizarUsuarioDTO data) {
        var usuarioAtualizado = authService.atualizarUsuario(data,userId);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @GetMapping("/users/me")
    public ResponseEntity<UsuarioRetornoDTO> obterUsuarioPorTokenJWT(@RequestHeader("Authorization") String authorizationHeader) {
        var tokenJWT = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "").trim();
        var usuario = authService.obterPorTokenJwt(tokenJWT);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/users/all")
    public ResponseEntity<Page<UsuarioRetornoDTO>> obterTodosUsuariosPaginados (@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "16") int size,
                                                             @RequestParam(defaultValue = "nome") String sortField,
                                                             @RequestParam(defaultValue = "asc") String sortOrder) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var usuariosPaginados = authService.obterUsuarios(pageable);
        return ResponseEntity.ok(usuariosPaginados);
    }

    @GetMapping("/users/role/{roleId}")
    public ResponseEntity<Page<UsuarioRetornoDTO>> obterUsuariosPorCargo (       @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "16") int size,
                                                        @RequestParam(defaultValue = "nome") String sortField,
                                                        @RequestParam(defaultValue = "asc") String sortOrder,
                                                        @PathVariable String roleId) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var usuariosPorCargo = authService.obterUsuariosPorCargo(roleId, pageable);
        return ResponseEntity.ok(usuariosPorCargo);
    }
}
