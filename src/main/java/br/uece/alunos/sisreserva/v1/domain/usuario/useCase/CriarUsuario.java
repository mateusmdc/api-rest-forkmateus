package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.DTOValidationException;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.InstituicaoService;
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
    private InstituicaoService instituicaoService;

    public UsuarioRetornoDTO criar(UsuarioDTO data) {
        boolean usuarioExiste = usuarioRepository.usuarioExistsByEmail(data.email());
        if (usuarioExiste) {
            throw new ValidationException("Email já cadastrado no sistema");
        }

        if (data.instituicaoId().isBlank()) {
            throw new DTOValidationException("O campo instituição está vazio.");
        }
        var instituicao = instituicaoService.obterEntidadePorId(data.instituicaoId());

        var novoUsuario = new Usuario(data, instituicao);

        System.out.println("Chamou até aqui, falta a validação que nem em add game list");
        System.out.println("falta a parte de criação dos usuários cargo");
        System.out.println("novo usuario");
        System.out.println(novoUsuario);
        System.out.println(novoUsuario.getEmail());
        System.out.println(novoUsuario.getId());

        //String senhaProtegida = bCryptPasswordEncoder.encode(data.senha());
        //novoUsuario.setSenha(senhaProtegida);

        //var usuarioNoBanco = usuarioRepository.save(novoUsuario);

        //return new UsuarioRetornoDTO(usuarioNoBanco);

        return null;
    }

}
