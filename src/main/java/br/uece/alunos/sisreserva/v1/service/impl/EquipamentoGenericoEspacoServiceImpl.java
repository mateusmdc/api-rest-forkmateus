package br.uece.alunos.sisreserva.v1.service.impl;

import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase.AtualizarQuantidadeEquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase.DesvincularEquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase.ObterEquipamentosGenericosEspaco;
import br.uece.alunos.sisreserva.v1.domain.equipamentoGenericoEspaco.useCase.VincularEquipamentoGenericoEspaco;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.AtualizarQuantidadeDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamentoGenericoEspaco.EquipamentoGenericoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoGenericoEspacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do serviço de gerenciamento do relacionamento entre equipamentos genéricos e espaços.
 * Coordena os casos de uso e garante transacionalidade das operações.
 * 
 * @author Sistema de Reservas - UECE
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class EquipamentoGenericoEspacoServiceImpl implements EquipamentoGenericoEspacoService {

    private final VincularEquipamentoGenericoEspaco vincularEquipamentoGenericoEspaco;
    private final AtualizarQuantidadeEquipamentoGenericoEspaco atualizarQuantidadeEquipamentoGenericoEspaco;
    private final DesvincularEquipamentoGenericoEspaco desvincularEquipamentoGenericoEspaco;
    private final ObterEquipamentosGenericosEspaco obterEquipamentosGenericosEspaco;

    @Override
    @Transactional
    public EquipamentoGenericoEspacoRetornoDTO vincular(EquipamentoGenericoEspacoDTO dto) {
        var vinculo = vincularEquipamentoGenericoEspaco.vincular(dto);
        return new EquipamentoGenericoEspacoRetornoDTO(vinculo);
    }

    @Override
    @Transactional
    public EquipamentoGenericoEspacoRetornoDTO atualizarQuantidade(String vinculoId, AtualizarQuantidadeDTO dto) {
        var vinculo = atualizarQuantidadeEquipamentoGenericoEspaco.atualizarQuantidade(vinculoId, dto);
        return new EquipamentoGenericoEspacoRetornoDTO(vinculo);
    }

    @Override
    @Transactional
    public void desvincular(String vinculoId) {
        desvincularEquipamentoGenericoEspaco.desvincular(vinculoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipamentoGenericoEspacoRetornoDTO> obterPorEspaco(String espacoId) {
        return obterEquipamentosGenericosEspaco.obterPorEspaco(espacoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipamentoGenericoEspacoRetornoDTO> obterPorEquipamento(String equipamentoGenericoId) {
        return obterEquipamentosGenericosEspaco.obterPorEquipamento(equipamentoGenericoId);
    }
}
