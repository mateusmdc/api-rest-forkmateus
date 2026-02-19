package br.uece.alunos.sisreserva.v1.service;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipamentoService {
    EquipamentoRetornoDTO atualizar(String id, EquipamentoAtualizarDTO data);
    EquipamentoRetornoDTO criar(EquipamentoDTO data);
    void deletar(String id);
    Page<EquipamentoRetornoDTO> obter(Pageable pageable, String id, String tombamento, String status, String tipoEquipamento, Boolean reservavel);
    
    /**
     * Obtém estatísticas de uso dos equipamentos.
     * 
     * @param mes mês para filtrar reservas (opcional, padrão = mês atual)
     * @param ano ano para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @return estatísticas agrupadas por equipamento
     */
    EstatisticasGeralEquipamentoDTO obterEstatisticas(Integer mes, Integer ano, List<String> equipamentoIds);
    
    /**
     * Gera PDF com estatísticas de uso dos equipamentos.
     * 
     * @param mes mês para filtrar reservas (opcional, padrão = mês atual)
     * @param ano ano para filtrar reservas (opcional, padrão = ano atual)
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @return array de bytes contendo o PDF gerado
     * @throws java.io.IOException se houver erro na geração do PDF
     */
    byte[] gerarPDFEstatisticas(Integer mes, Integer ano, List<String> equipamentoIds) throws java.io.IOException;
}
