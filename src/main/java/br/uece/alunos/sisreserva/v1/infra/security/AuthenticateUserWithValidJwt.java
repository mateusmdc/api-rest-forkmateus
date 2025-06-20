package br.uece.alunos.sisreserva.v1.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;

@Component
public class AuthenticateUserWithValidJwt {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findUserAuthenticated(String email) {
        return (Usuario) usuarioRepository.findByEmail(email);
    }

}
