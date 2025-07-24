package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.service.UtilsService;
import org.springframework.stereotype.Service;

import java.text.Normalizer;


@Service
public class UtilsServiceImpl implements UtilsService {
    @Override
    public String normalizeString(String value) {
        if (value == null) return "";
        return Normalizer
                .normalize(value.trim().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\p{ASCII}]", "");
    }
}
