package br.uece.alunos.sisreserva.v1.domain.comiteUsuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.ComiteUsuarioRepository;
import br.uece.alunos.sisreserva.v1.domain.comiteUsuario.validation.ComiteUsuarioValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeletarComiteUsuario {
    private final ComiteUsuarioRepository repository;
    private final ComiteUsuarioValidator validator;

    @Transactional
    public void deletar(String id) {
        validator.validarComiteUsuarioId(id);

        var comiteUsuario = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Comitê de Usuário deveria existir após validação, mas não foi encontrado."));

        repository.delete(comiteUsuario);
    }
}
