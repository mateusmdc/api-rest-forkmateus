package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.TokenDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
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

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity<ApiResponseDTO<UsuarioRetornoDTO>> criarUsuario(@RequestBody @Valid UsuarioDTO data) {
        var novoUsuarioDTO = authService.criarUsuario(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(novoUsuarioDTO));
    }

    @PostMapping("/senha/esqueci")
    @Transactional
    public ResponseEntity<ApiResponseDTO<String>> esqueciSenha(@RequestBody @Valid UsuarioEmailDTO data) {
        var messageResponseDTO = authService.esqueciMinhaSenha(data);
        return ResponseEntity.ok(ApiResponseDTO.success(messageResponseDTO.message()));
    }

    @PostMapping("/senha/resetar")
    @Transactional
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(@RequestBody @Valid UsuarioTrocarSenhaDTO data) {
        var messageResponseDTO = authService.resetarSenha(data);
        return ResponseEntity.ok(ApiResponseDTO.success(messageResponseDTO.message()));
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<ApiResponseDTO<TokenDTO>> realizarLogin(@RequestBody @Valid UsuarioLoginDTO data,
                                                                       HttpServletResponse response,
                                                                       HttpServletRequest request) {
        var tokensJwt = authService.login(data, request);
        cookieManager.addRefreshTokenCookie(response, tokensJwt.refreshToken());
        return ResponseEntity.ok(ApiResponseDTO.success(new TokenDTO(tokensJwt.accessToken())));
    }

    @PutMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponseDTO<UsuarioRetornoDTO>> atualizarUsuario(@PathVariable String idUsuario,
                                                                              @RequestBody AtualizarUsuarioDTO data) {
        var usuarioAtualizado = authService.atualizarUsuario(data, idUsuario);
        return ResponseEntity.ok(ApiResponseDTO.success(usuarioAtualizado));
    }

    @GetMapping("/usuario/me")
    public ResponseEntity<ApiResponseDTO<UsuarioRetornoDTO>> obterUsuarioPorTokenJWT(@RequestHeader("Authorization") String authorizationHeader) {
        var tokenJWT = authorizationHeader.replaceFirst("(?i)^Bearer\\s+", "").trim();
        var usuario = authService.obterPorTokenJwt(tokenJWT);
        return ResponseEntity.ok(ApiResponseDTO.success(usuario));
    }

    @GetMapping("/usuario")
    public ResponseEntity<ApiResponseDTO<Page<UsuarioRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) Integer matricula,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String documentoFiscal,
            @RequestParam(required = false) String instituicaoId,
            @RequestParam(required = false) String cargoId,
            @RequestParam(required = false) String nome
    ) {
        String fieldToSort = switch (sortField) {
            case "nome" -> "nome";
            case "email" -> "email";
            case "matricula" -> "matricula";
            default -> sortField;
        };

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), fieldToSort));

        var resultado = authService.obter(
                pageable,
                id,
                matricula,
                email,
                documentoFiscal,
                instituicaoId,
                cargoId,
                nome
        );

        return ResponseEntity.ok(ApiResponseDTO.success(resultado));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<TokenDTO>> atualizarAccessToken(HttpServletRequest request) {
        var refreshToken = cookieManager.getRefreshTokenFromCookie(request);
        var novoAccessToken = authService.atualizarToken(refreshToken);
        return ResponseEntity.ok(ApiResponseDTO.success(novoAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout(HttpServletResponse response) {
        cookieManager.removeRefreshTokenCookie(response);
        return ResponseEntity.ok(ApiResponseDTO.success("Logout realizado com sucesso."));
    }
}