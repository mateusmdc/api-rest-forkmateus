package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarUsuario {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityHandlerService entityHandlerService;
    private final UsuarioCargoService usuarioCargoService;
    private final UsuarioRepository repository;
    private final UsuarioValidator validator;

    public UsuarioRetornoDTO criar(UsuarioDTO data) {
        try {
            validator.validarEmailJaExistente(data.email());

            var instituicao = entityHandlerService.obterInstituicaoPorId(data.instituicaoId());

            var novoUsuario = new Usuario(data, instituicao);
            String senhaProtegida = bCryptPasswordEncoder.encode(data.senha());
            novoUsuario.setSenha(senhaProtegida);

            var usuarioNoBanco = repository.save(novoUsuario);

            if (data.cargosNome() != null && !data.cargosNome().isEmpty()) {
                var cargaDTO = new CriarCargaUsuarioCargoDTO(usuarioNoBanco.getId(), data.cargosNome());
                var listaUsuariosCargo = usuarioCargoService.criarEmCargaUsuarioCargo(cargaDTO);

                //apos persistir o usuario, os cargos ainda nao estao carregados em usuario.getUsuarioCargos()
                //(provavelmente por causa do contexto de persistencia e ausencia de fetch automatico).
                //para evitar consultas desnecessarias ao banco e garantir que o dto tenha os dados corretos,
                //utilizo diretamente o retorno da criação da relação que ja traz os cargos vinculados.
                return new UsuarioRetornoDTO(usuarioNoBanco, listaUsuariosCargo);
            }

            return new UsuarioRetornoDTO(usuarioNoBanco);
        } catch (Exception e) {
            throw new ValidationException("Erro ao criar usuário: " + e.getMessage());
        }
    }
}