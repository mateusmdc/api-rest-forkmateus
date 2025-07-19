package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorGestorEspaco {

    @Autowired
    private GestorEspacoRepository repository;

    public void validarGestorAtivo(String usuarioId, String espacoId) {
        boolean ehGestor = repository.existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(usuarioId, espacoId);
        if (!ehGestor) {
            throw new ValidationException("Usuário não é gestor ativo do espaço informado.");
        }
    }
}

