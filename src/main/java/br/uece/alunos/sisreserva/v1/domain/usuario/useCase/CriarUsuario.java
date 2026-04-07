package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.credencialLocal.CredencialLocal;
import br.uece.alunos.sisreserva.v1.domain.credencialLocal.CredencialLocalRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.usuarioCargo.CriarCargaUsuarioCargoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.DocumentoFiscalUtils;
import br.uece.alunos.sisreserva.v1.infra.utils.validators.TelefoneUtils;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarUsuario {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityHandlerService entityHandlerService;
    private final UsuarioCargoService usuarioCargoService;
    private final UsuarioRepository repository;
    private final CredencialLocalRepository credencialLocalRepository;
    private final UsuarioValidator validator;

    public UsuarioRetornoDTO criar(UsuarioDTO data) {
        try {
            if (data.cargosNome() != null && data.cargosNome().contains("USUARIO_INTERNO")) {
                throw new ValidationException(
                        "Usuários internos são provisionados automaticamente no primeiro login institucional."
                );
            }

            validator.validarEmailJaExistente(data.email());

            String cpfNormalizado = DocumentoFiscalUtils.normalizarCPF(data.documentoFiscal());
            validator.validarCPF(cpfNormalizado);
            validator.validarCPFJaExistente(cpfNormalizado);

            String telefoneNormalizado = TelefoneUtils.normalizarTelefone(data.telefone());
            validator.validarTelefone(telefoneNormalizado);

            var instituicao = entityHandlerService.obterInstituicaoPorId(data.instituicaoId());

            var novoUsuario = new Usuario(data, instituicao);
            novoUsuario.setDocumentoFiscal(cpfNormalizado);
            novoUsuario.setTelefone(telefoneNormalizado);

            var usuarioNoBanco = repository.save(novoUsuario);

            var credencial = new CredencialLocal(
                    usuarioNoBanco,
                    bCryptPasswordEncoder.encode(data.senha())
            );
            credencialLocalRepository.save(credencial);

            if (data.cargosNome() != null && !data.cargosNome().isEmpty()) {
                var cargaDTO = new CriarCargaUsuarioCargoDTO(usuarioNoBanco.getId(), data.cargosNome());
                var listaUsuariosCargo = usuarioCargoService.criarEmCargaUsuarioCargo(cargaDTO);
                return new UsuarioRetornoDTO(usuarioNoBanco, listaUsuariosCargo);
            }

            return new UsuarioRetornoDTO(usuarioNoBanco);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException("Erro ao criar usuário: " + e.getMessage());
        }
    }
}