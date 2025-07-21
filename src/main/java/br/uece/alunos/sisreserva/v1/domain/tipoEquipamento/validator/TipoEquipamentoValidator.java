package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.validator;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamentoRepository;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class TipoEquipamentoValidator {
    @Autowired
    private TipoEquipamentoRepository repository;

    public void validarNomeDuplicado(TipoEquipamentoDTO data) {
        var nomeNovo = normalize(data.nome());

        boolean existe = repository.findAll().stream()
                .map(te -> normalize(te.getNome()))
                .anyMatch(nomeExistente -> nomeExistente.equals(nomeNovo));

        if (existe) {
            throw new ValidationException("Já existe um tipo de equipamento com esse nome.");
        }
    }

    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer
                .normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("\\s+", " ");
    }
}
