package br.uece.alunos.sisreserva.v1.domain.espaco.validation;

import br.uece.alunos.sisreserva.v1.domain.espaco.EspacoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class EspacoValidator {
    @Autowired
    private EspacoRepository repository;

    public void validarSeEspacoJaExiste(String nome, String departamentoId, String localizacaoId) {
        String nomeNormalizado = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .trim()
                .toLowerCase();

        boolean jaExiste = repository.existsByNomeDepartamentoAndLocalizacao(
                nomeNormalizado, departamentoId, localizacaoId
        );

        if (jaExiste) {
            throw new IllegalArgumentException("Já existe um espaço com esse nome, departamento e localização.");
        }
    }
}
