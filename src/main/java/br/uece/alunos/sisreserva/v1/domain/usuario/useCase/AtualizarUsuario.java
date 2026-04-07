package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.credencialLdap.CredencialLdapRepository;
import br.uece.alunos.sisreserva.v1.domain.credencialLocal.CredencialLocalRepository;
import br.uece.alunos.sisreserva.v1.domain.instituicao.Instituicao;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.validation.UsuarioValidator;
import br.uece.alunos.sisreserva.v1.dto.usuario.AtualizarUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import br.uece.alunos.sisreserva.v1.service.UsuarioCargoService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarUsuario {

    private final UsuarioRepository repository;
    private final CredencialLocalRepository credencialLocalRepository;
    private final CredencialLdapRepository credencialLdapRepository;
    private final UsuarioValidator validator;
    private final EntityHandlerService entityHandlerService;
    private final UsuarioCargoService usuarioCargoService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioRetornoDTO atualizarUsuario(AtualizarUsuarioDTO data, String idUsuario) {
        validator.validarUsuarioId(idUsuario);

        var usuarioNoBanco = repository.findById(idUsuario)
                .orElseThrow(() -> new IllegalStateException(
                        "Usuário não encontrado após validação de ID."
                ));

        boolean isUsuarioInterno = credencialLdapRepository.findByUsuarioId(idUsuario).isPresent();

        if (isUsuarioInterno && data.senha() != null && !data.senha().isBlank()) {
            throw new ValidationException(
                    "Usuários internos não possuem senha local gerenciada pelo sistema."
            );
        }

        if (!isUsuarioInterno && data.senha() != null && !data.senha().isBlank()) {
            var credencial = credencialLocalRepository.findByUsuarioId(idUsuario)
                    .orElseThrow(() -> new IllegalStateException(
                            "Credencial local não encontrada para usuário externo."
                    ));
            credencial.setSenha(bCryptPasswordEncoder.encode(data.senha()));
            credencialLocalRepository.save(credencial);
        }

        Instituicao instituicao = data.instituicaoId() != null
                ? entityHandlerService.obterInstituicaoPorId(data.instituicaoId())
                : null;

        usuarioNoBanco.atualizarUsuario(data, instituicao);

        var usuarioAtualizado = repository.save(usuarioNoBanco);

        if (data.cargosId() != null && !data.cargosId().isEmpty()) {
            usuarioCargoService.atualizarCargos(data.cargosId(), usuarioAtualizado.getId());
        }

        usuarioAtualizado = repository.findByIdToHandle(idUsuario);

        return new UsuarioRetornoDTO(usuarioAtualizado);
    }
}