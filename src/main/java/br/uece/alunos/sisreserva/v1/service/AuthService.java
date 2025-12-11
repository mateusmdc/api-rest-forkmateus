package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.TokenDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthService {
    TokenDTO atualizarToken(String refreshToken);
    UsuarioRetornoDTO atualizarUsuario(AtualizarUsuarioDTO data, String idUsuario);
    UsuarioRetornoDTO criarUsuario(UsuarioDTO data);
    MessageResponseDTO esqueciMinhaSenha(UsuarioEmailDTO data);
    AuthTokensDTO login(UsuarioLoginDTO data, HttpServletRequest request);
    UsuarioRetornoDTO obterPorTokenJwt(String tokenJWT);
    Page<UsuarioRetornoDTO> obter(Pageable pageable,
                                  String id,
                                  String matricula,
                                  String email,
                                  String documentoFiscal,
                                  String instituicaoId,
                                  String cargoId,
                                  String nome);
    void logout(HttpServletRequest request, HttpServletResponse response);
    MessageResponseDTO resetarSenha(UsuarioTrocarSenhaDTO data);
}
