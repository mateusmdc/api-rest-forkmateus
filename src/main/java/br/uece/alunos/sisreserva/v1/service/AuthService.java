package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioEmailDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioLoginDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.security.AuthTokensDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UsuarioRetornoDTO criarUsuario(UsuarioDTO data);
    AuthTokensDTO signIn(UsuarioLoginDTO data, HttpServletRequest request);
    boolean esqueciMinhaSenha(UsuarioEmailDTO data);
}
