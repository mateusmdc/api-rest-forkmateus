package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.dto.usuario.*;
import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.CriarUsuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.EsqueciSenha;
import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.RealizarLogin;
import br.uece.alunos.sisreserva.v1.dto.utils.AuthTokensDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import br.uece.alunos.sisreserva.v1.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private CriarUsuario criarUsuario;
    @Autowired
    private EsqueciSenha esqueciSenha;
    @Autowired
    private RealizarLogin realizarLogin;

    @Override
    public UsuarioRetornoDTO criarUsuario(UsuarioDTO data) {
        return criarUsuario.criar(data);
    }

    @Override
    public AuthTokensDTO signIn(UsuarioLoginDTO data, HttpServletRequest request) {
        return realizarLogin.signIn(data, request);
    }

    @Override
    public MessageResponseDTO esqueciMinhaSenha(UsuarioEmailDTO data) {
        return esqueciSenha.esqueciMinhaSenha(data);
    }
}
