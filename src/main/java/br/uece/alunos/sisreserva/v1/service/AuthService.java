package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthService {
    UsuarioRetornoDTO criarUsuario(UsuarioDTO data);
    MessageResponseDTO esqueciMinhaSenha(UsuarioEmailDTO data);
    AuthTokensDTO login(UsuarioLoginDTO data, HttpServletRequest request);
    UsuarioRetornoDTO obterPorTokenJwt(String tokenJWT);
    Page<UsuarioRetornoDTO> obterUsuarios(Pageable pageable);
    Page<UsuarioRetornoDTO> obterUsuariosPorCargo(String cargoId, Pageable pageable);
    MessageResponseDTO resetarSenha(UsuarioTrocarSenhaDTO data);
}
