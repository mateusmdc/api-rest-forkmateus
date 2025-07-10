package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GestorEspacoValidator {

    @Autowired
    private GestorEspacoRepository repository;

    public void validarSeUsuarioJaEGestorDoEspaco(String usuarioId, String espacoId) {
        boolean jaEGestor = repository.existsByUsuarioGestorIdAndEspacoId(usuarioId, espacoId);

        if (jaEGestor) {
            throw new ValidationException("O usuário já é o gestor do espaço.");
        }
    }
}
