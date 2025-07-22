package br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.validation;

import br.uece.alunos.sisreserva.v1.domain.equipamentoEspaco.EquipamentoEspacoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoEspacoValidator {
    @Autowired
    private EquipamentoEspacoRepository repository;

    public void validarEquipamentoEspacoId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("O ID do equipamento espaço não pode ser nulo ou vazio.");
        }

        if (!repository.existsById(id)) {
            throw new ValidationException("Equipamento alocado ao espaço com o ID fornecido não existe.");
        }
    }
}
