package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.cargo.useCase.ObterCargos;
import br.uece.alunos.sisreserva.v1.dto.cargo.CargoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CargoServiceImpl implements CargoService {
    @Autowired
    private ObterCargos obterCargos;

    @Override
    public Page<CargoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterCargos.obter(pageable, id, nome);
    }
}
