package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.tipoEspaco.useCase.ObterTiposEspaco;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEspacoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TipoEspacoServiceImpl implements TipoEspacoService {
    @Autowired
    private ObterTiposEspaco obterTiposEspaco;

    @Override
    public Page<TipoEspacoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterTiposEspaco.obter(pageable, id, nome);
    }
}
