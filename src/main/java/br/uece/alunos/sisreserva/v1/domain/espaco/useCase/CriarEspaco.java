package br.uece.alunos.sisreserva.v1.domain.espaco.useCase;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class CriarEspaco {
    @Autowired
    private EspacoRepository repository;

    public EspacoRetornoDTO criarEspaco(EspacoDTO data) {
        var nomeNormalizado = Normalizer.normalize(data.nome(), Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // remove acentos
                .trim()
                .toLowerCase();
        if (repository.existsByNome(nomeNormalizado)) {
            throw new RuntimeException("Espaço com esse nome já existe");
        }

        //deve buscar o departamento entidade


        //deve buscar a localizacao entidade

        //deve buscar o tipoEspaco entidade

        //deve buscar o tipoAtividade entidade

        return null;
    }

}
