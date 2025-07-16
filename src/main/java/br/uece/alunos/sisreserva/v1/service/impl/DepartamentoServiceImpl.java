package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.departamento.useCase.ObterDepartamentos;
import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {
    @Autowired
    private ObterDepartamentos obterDepartamentos;

    @Override
    public Page<DepartamentoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterDepartamentos.obter(pageable, id, nome);
    }
}
