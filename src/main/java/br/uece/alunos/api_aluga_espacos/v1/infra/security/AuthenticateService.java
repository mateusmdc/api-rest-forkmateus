package br.uece.alunos.api_aluga_espacos.v1.infra.security;

import br.uece.alunos.api_aluga_espacos.v1.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = usuarioRepository.findByEmail(username);
        if (userDetails == null) {
            //userDetails = usuarioRepository.findByMatricula(matricula); mas campo matricula vai ser unico?
        }
        return userDetails;
    }
}
