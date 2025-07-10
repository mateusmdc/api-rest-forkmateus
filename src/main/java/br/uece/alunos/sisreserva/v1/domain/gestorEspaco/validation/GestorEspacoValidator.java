package br.uece.alunos.sisreserva.v1.domain.gestorEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.gestorEspaco.GestorEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GestorEspacoValidator {
    @Autowired
    private GestorEspacoRepository repository;

    public void validarGestorAtivoExistente(String usuarioId, String espacoId) {
        boolean existeAtivo = repository.existsByUsuarioGestorIdAndEspacoIdAndEstaAtivoTrue(usuarioId, espacoId);
        if (existeAtivo) {
            throw new ValidationException("O usuário já é gestor ativo do espaço.");
        }
    }

    public void validarGestorAtivoParaInativar(String gestorEspacoId) {
        var gestorEspaco = repository.findById(gestorEspacoId)
                .orElseThrow(() -> new ValidationException("Gestor do espaço não encontrado."));

        if (!gestorEspaco.getEstaAtivo()) {
            throw new ValidationException("O gestor já está inativo.");
        }
    }
}
