package br.uece.alunos.sisreserva.v1.domain.cargo.useCase;

import br.uece.alunos.sisreserva.v1.domain.cargo.Cargo;
import br.uece.alunos.sisreserva.v1.domain.cargo.CargoRepository;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObterEntListaCargoPorNome {
    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> obterEntidadesCargoPorNome(List<String> nomes) {
        List<String> nomesNormalizados = nomes.stream()
                .map(nome -> nome.trim().toLowerCase())
                .toList();

        var cargos = cargoRepository.findAllByNomesIgnoreCaseAndTrimmed(nomesNormalizados);

        if (cargos.isEmpty()) {
            throw new ValidationException("Não foram encontrados cargos a partir da lista de nomes passados como parâmetro.");
        }

        return cargos;
    }
}
