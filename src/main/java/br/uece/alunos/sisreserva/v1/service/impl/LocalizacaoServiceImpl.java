package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.localizacao.useCase.ObterLocalizacoes;
import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.LocalizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoServiceImpl implements LocalizacaoService {
    @Autowired
    private ObterLocalizacoes obterLocalizacoes;

    @Override
    public Page<LocalizacaoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterLocalizacoes.obter(pageable, id, nome);
    }
}
