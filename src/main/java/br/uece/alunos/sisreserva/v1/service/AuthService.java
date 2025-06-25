package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UsuarioRetornoDTO criarUsuario(UsuarioDTO data);
    AuthTokensDTO signIn(UsuarioLoginDTO data, HttpServletRequest request);
    boolean esqueciMinhaSenha(UsuarioEmailDTO data);
}
