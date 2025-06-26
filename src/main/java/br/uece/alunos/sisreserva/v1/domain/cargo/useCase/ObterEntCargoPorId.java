package br.uece.alunos.sisreserva.v1.domain.cargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import br.uece.alunos.sisreserva.v1.domain.cargo.CargoRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObterEntCargoPorId {
    @Autowired
    private CargoRepository cargoRepository;

    public Cargo obterEntidadeCargoPorId(String id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Não foram encontrados cargos a partir da lista de nomes passados como parâmetro."));
    }
}
