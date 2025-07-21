package br.uece.alunos.sisreserva.v1.domain.comite.validation;

import br.uece.alunos.sisreserva.v1.domain.comite.Comite;
import br.uece.alunos.sisreserva.v1.domain.comite.ComiteRepository;
import br.uece.alunos.sisreserva.v1.domain.comite.TipoComite;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.List;

@Component
public class ComiteValidator {

    @Autowired
    private ComiteRepository repository;

    public void validarDescricaoDuplicada(ComiteDTO data) {
        String descricaoNova = normalize(data.descricao());
        TipoComite tipo = data.tipo();

        List<Comite> existentes = repository.findByTipo(tipo);

        boolean existeDuplicado = existentes.stream()
                .map(comite -> normalize(comite.getDescricao()))
                .anyMatch(descricaoExistente -> descricaoExistente.equals(descricaoNova));

        if (existeDuplicado) {
            throw new ValidationException("Já existe um comitê com essa descrição para o tipo informado.");
        }
    }

    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer
                .normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")          // remove acentos
                .replaceAll("[^\\p{ASCII}]", "")   // remove caracteres especiais (não ASCII)
                .replaceAll("\\s+", " ");          // normaliza espaços
    }
}
