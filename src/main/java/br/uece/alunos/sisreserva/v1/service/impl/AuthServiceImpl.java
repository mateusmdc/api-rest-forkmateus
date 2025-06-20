package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.useCase.CriarUsuario;
import br.uece.alunos.sisreserva.v1.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    @Autowired
    private CriarUsuario criarUsuario;

    @Override
    public UsuarioRetornoDTO criarUsuario(UsuarioDTO data) {
        return criarUsuario.criar(data);
    }
}
