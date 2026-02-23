package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.TokenDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.infra.security.UsuarioAutenticadoService;
import br.uece.alunos.sisreserva.v1.infra.utils.httpCookies.CookieManager;
import br.uece.alunos.sisreserva.v1.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Rotas de autenticação mapeadas no controller")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    @PostMapping("/usuario")
    @Transactional
    public ResponseEntity<ApiResponseDTO<UsuarioRetornoDTO>> criarUsuario(
            @RequestBody @Valid UsuarioDTO data) {

        var novoUsuarioDTO = authService.criarUsuario(data);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(novoUsuarioDTO));
    }

    @PutMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponseDTO<UsuarioRetornoDTO>> atualizarUsuario(
            @PathVariable String idUsuario,
            @RequestBody AtualizarUsuarioDTO data) {

        var usuarioAtualizado = authService.atualizarUsuario(data, idUsuario);
        return ResponseEntity.ok(ApiResponseDTO.success(usuarioAtualizado));
    }

    @GetMapping("/usuario/me")
    public ResponseEntity<ApiResponseDTO<UsuarioRetornoDTO>> obterUsuarioAtual() {
        Usuario usuarioAutenticado = usuarioAutenticadoService.getUsuarioAutenticado();
        var usuarioRetorno = authService.obterPorId(usuarioAutenticado.getId());
        return ResponseEntity.ok(ApiResponseDTO.success(usuarioRetorno));
    }

    @GetMapping("/usuario")
    public ResponseEntity<ApiResponseDTO<Page<UsuarioRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0")    int    page,
            @RequestParam(defaultValue = "100")  int    size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc")  String sortOrder,
            @RequestParam(required = false)       String id,
            @RequestParam(required = false)       String matricula,
            @RequestParam(required = false)       String email,
            @RequestParam(required = false)       String documentoFiscal,
            @RequestParam(required = false)       String instituicaoId,
            @RequestParam(required = false)       String cargoId,
            @RequestParam(required = false)       String nome) {

        String fieldToSort = switch (sortField) {
            case "nome"      -> "nome";
            case "email"     -> "email";
            case "matricula" -> "matricula";
            default          -> sortField;
        };

        var pageable  = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortOrder), fieldToSort));
        var resultado = authService.obter(
                pageable, id, matricula, email,
                documentoFiscal, instituicaoId, cargoId, nome);

        return ResponseEntity.ok(ApiResponseDTO.success(resultado));
    }

    @PostMapping("/senha/esqueci")
    @Transactional
    public ResponseEntity<ApiResponseDTO<String>> esqueciSenha(
            @RequestBody @Valid UsuarioEmailDTO data) {

        var msg = authService.esqueciMinhaSenha(data);
        return ResponseEntity.ok(ApiResponseDTO.success(msg.message()));
    }

    @PostMapping("/senha/resetar")
    @Transactional
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(
            @RequestBody @Valid UsuarioTrocarSenhaDTO data) {

        var msg = authService.resetarSenha(data);
        return ResponseEntity.ok(ApiResponseDTO.success(msg.message()));
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<ApiResponseDTO<TokenDTO>> realizarLogin(
            @RequestBody @Valid UsuarioLoginDTO data,
            HttpServletResponse response,
            HttpServletRequest request) {

        var tokens = authService.login(data, request);

        cookieManager.addAccessTokenCookie(response, tokens.accessToken());
        cookieManager.addRefreshTokenCookie(response, tokens.refreshToken());

        return ResponseEntity.ok(ApiResponseDTO.success(new TokenDTO(tokens.accessToken())));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<TokenDTO>> atualizarAccessToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        var refreshToken    = cookieManager.getRefreshTokenFromCookie(request);
        var novoAccessToken = authService.atualizarToken(refreshToken);

        cookieManager.addAccessTokenCookie(response, novoAccessToken.token());

        return ResponseEntity.ok(ApiResponseDTO.success(novoAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout(
            HttpServletResponse response,
            HttpServletRequest request) {

        authService.logout(request, response);
        return ResponseEntity.ok(ApiResponseDTO.success("Logout realizado com sucesso."));
    }
}