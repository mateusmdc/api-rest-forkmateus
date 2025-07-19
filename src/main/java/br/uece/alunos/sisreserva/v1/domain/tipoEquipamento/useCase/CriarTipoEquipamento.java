package br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.useCase;

import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamento;
import br.uece.alunos.sisreserva.v1.domain.tipoEquipamento.TipoEquipamentoRepository;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class CriarTipoEquipamento {

    @Autowired
    private TipoEquipamentoRepository repository;

    public TipoEquipamentoRetornoDTO criar(TipoEquipamentoDTO data) {
        var nomeNovo = normalize(data.nome());

        var existe = repository.findAll().stream()
                .map(te -> normalize(te.getNome()))
                .anyMatch(nomeExistente -> nomeExistente.equals(nomeNovo));

        if (existe) {
            throw new ValidationException("Já existe um tipo de equipamento com esse nome.");
        }

        var tipoEquipamento = new TipoEquipamento(data);

        var salvo = repository.save(tipoEquipamento);

        return new TipoEquipamentoRetornoDTO(salvo);
    }

    private String normalize(String value) {
        if (value == null) return "";
        return Normalizer
                .normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")               // remove acentos
                .replaceAll("[^\\p{ASCII}]", "")        // remove caracteres não-ASCII
                .replaceAll("\\s+", " ");               // normaliza espaços
    }
}
