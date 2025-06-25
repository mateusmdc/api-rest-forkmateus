package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UsuarioRetornoDTO criarUsuario(UsuarioDTO data);
    MessageResponseDTO esqueciMinhaSenha(UsuarioEmailDTO data);
    AuthTokensDTO login(UsuarioLoginDTO data, HttpServletRequest request);
    UsuarioRetornoDTO obterPorTokenJwt(String tokenJWT);
    MessageResponseDTO resetarSenha(UsuarioTrocarSenhaDTO data);
}
