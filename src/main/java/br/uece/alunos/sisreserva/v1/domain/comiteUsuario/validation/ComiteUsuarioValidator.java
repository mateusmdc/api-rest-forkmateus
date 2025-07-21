package br.uece.alunos.sisreserva.v1.domain.comiteUsuario.validation;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComiteUsuarioValidator {

    @Autowired
    private ComiteUsuarioRepository repository;

    public void validarUsuarioParaComite(Usuario usuario, Comite comite) {
        boolean usuarioJaAlocado = repository.findByComiteId(comite.getId()).stream()
                .anyMatch(cu -> cu.getUsuario().getId().equals(usuario.getId()));

        if (usuarioJaAlocado) {
            throw new ValidationException("O usuário já está alocado para este comitê.");
        }

        // Se for representante_discente, o usuário deve ter o cargo ALUNO
        if (comite.getTipo() == TipoComite.REPRESENTANTE_DISCENTE) {
            boolean possuiCargoAluno = usuario.getRoles().stream()
                    .anyMatch(role -> role.equalsIgnoreCase("ALUNO"));

            if (!possuiCargoAluno) {
                throw new ValidationException("Usuário deve possuir o cargo ALUNO para ser membro do comitê de representantes discente.");
            }
        }
    }
}
