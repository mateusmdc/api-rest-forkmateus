package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuarioCargo.DTO.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
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

    @Autowired
    private UsuarioCargoService usuarioCargoService;

    public UsuarioRetornoDTO criar(UsuarioDTO data) {
        try {
            boolean usuarioExiste = usuarioRepository.usuarioExistsByEmail(data.email());
            if (usuarioExiste) {
                throw new ValidationException("Email já cadastrado no sistema");
            }

            var instituicao = entityHandlerService.obterInstituicaoPorId(data.instituicaoId());

            var novoUsuario = new Usuario(data, instituicao);
            String senhaProtegida = bCryptPasswordEncoder.encode(data.senha());
            novoUsuario.setSenha(senhaProtegida);

            var usuarioNoBanco = usuarioRepository.save(novoUsuario);

            if (data.cargosNome() != null && !data.cargosNome().isEmpty()) {
                var cargaDTO = new CriarCargaUsuarioCargoDTO(usuarioNoBanco.getId(), data.cargosNome());
                usuarioCargoService.criarEmCargaUsuarioCargo(cargaDTO);
                // atualiza as relações de cargos após persistência
                usuarioNoBanco = usuarioRepository.findById(usuarioNoBanco.getId()).orElseThrow();
            }

            return new UsuarioRetornoDTO(usuarioNoBanco);

        } catch (Exception e) {
            throw new ValidationException("Erro ao criar usuário: " + e.getMessage());
        }
    }
}