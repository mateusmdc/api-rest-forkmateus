package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.*;
import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import br.uece.alunos.sisreserva.v1.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private CriarUsuario criarUsuario;
    @Autowired
    private EsqueciSenha esqueciSenha;
    @Autowired
    private ObterUsuarioPorJWT obterUsuarioPorJWT;
    @Autowired
    private ObterUsuariosPorCargoId obterUsuariosPorCargoId;
    @Autowired
    private ObterUsuariosPaginado obterUsuariosPaginado;
    @Autowired
    private RealizarLogin realizarLogin;
    @Autowired
    private TrocarSenha trocarSenha;

    @Override
    public UsuarioRetornoDTO criarUsuario(UsuarioDTO data) {
        return criarUsuario.criar(data);
    }

    @Override
    public MessageResponseDTO esqueciMinhaSenha(UsuarioEmailDTO data) {
        return esqueciSenha.esqueciMinhaSenha(data);
    }

    @Override
    public AuthTokensDTO login(UsuarioLoginDTO data, HttpServletRequest request) {
        return realizarLogin.login(data, request);
    }

    @Override
    public UsuarioRetornoDTO obterPorTokenJwt(String tokenJWT) {
        return obterUsuarioPorJWT.obterPorTokenJwt(tokenJWT);
    }

    @Override
    public Page<UsuarioRetornoDTO> obterUsuarios(Pageable pageable) {
        return obterUsuariosPaginado.obterUsuarios(pageable);
    }

    @Override
    public Page<UsuarioRetornoDTO> obterUsuariosPorCargo(String cargoId, Pageable pageable) {
        return obterUsuariosPorCargoId.obterUsuariosPorCargo(cargoId, pageable);
    }

    @Override
    public MessageResponseDTO resetarSenha(UsuarioTrocarSenhaDTO data) {
        return trocarSenha.resetarSenha(data);
    }
}
