package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CriarUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EntityHandlerService entityHandlerService;

    public UsuarioRetornoDTO criar(UsuarioDTO data) {
        boolean usuarioExiste = usuarioRepository.usuarioExistsByEmail(data.email());
        if (usuarioExiste) {
            throw new ValidationException("Email já cadastrado no sistema");
        }

        var instituicao = entityHandlerService.obterInstituicaoPorId(data.instituicaoId());

        var novoUsuario = new Usuario(data, instituicao);

        String senhaProtegida = bCryptPasswordEncoder.encode(data.senha());
        novoUsuario.setSenha(senhaProtegida);

        var usuarioNoBanco = usuarioRepository.save(novoUsuario);

        //FALTA AQUI A CHAMADA PARA A CRIAÇÃO DE USUARIO_CARGO

        return new UsuarioRetornoDTO(usuarioNoBanco);
    }

}
