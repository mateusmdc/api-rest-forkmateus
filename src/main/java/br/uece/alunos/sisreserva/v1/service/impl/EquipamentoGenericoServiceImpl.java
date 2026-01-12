package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenerico.useCase.*;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenerico.EquipamentoGenericoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoGenericoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do serviço de gerenciamento de equipamentos genéricos.
 * Coordena os casos de uso e garante transacionalidade das operações.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class EquipamentoGenericoServiceImpl implements EquipamentoGenericoService {

    private final CriarEquipamentoGenerico criarEquipamentoGenerico;
    private final ObterEquipamentosGenericos obterEquipamentosGenericos;
    private final ObterEquipamentoGenericoPorId obterEquipamentoGenericoPorId;
    private final AtualizarEquipamentoGenerico atualizarEquipamentoGenerico;
    private final DeletarEquipamentoGenerico deletarEquipamentoGenerico;

    @Override
    @Transactional
    public EquipamentoGenericoRetornoDTO criar(EquipamentoGenericoDTO dto) {
        var equipamento = criarEquipamentoGenerico.criar(dto);
        return new EquipamentoGenericoRetornoDTO(equipamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EquipamentoGenericoRetornoDTO> obter(Pageable pageable, String id, String nome) {
        return obterEquipamentosGenericos.obter(pageable, id, nome);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipamentoGenericoRetornoDTO obterPorId(String id) {
        var equipamento = obterEquipamentoGenericoPorId.obter(id);
        return new EquipamentoGenericoRetornoDTO(equipamento);
    }

    @Override
    @Transactional
    public EquipamentoGenericoRetornoDTO atualizar(String id, EquipamentoGenericoDTO dto) {
        var equipamento = atualizarEquipamentoGenerico.atualizar(id, dto);
        return new EquipamentoGenericoRetornoDTO(equipamento);
    }

    @Override
    @Transactional
    public void deletar(String id) {
        deletarEquipamentoGenerico.deletar(id);
    }
}
