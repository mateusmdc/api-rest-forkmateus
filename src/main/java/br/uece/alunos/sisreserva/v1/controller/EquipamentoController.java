package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.equipamento.EstatisticasGeralEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipamento")
@Tag(name = "Rotas de equipamento mapeadas no controller")
public class EquipamentoController {
    @Autowired
    private EquipamentoService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<EquipamentoRetornoDTO>> criar(@RequestBody @Valid EquipamentoDTO data) {
        var equipamentoCriado = service.criar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(equipamentoCriado));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<EquipamentoRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "tombamento") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String tombamento,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipoEquipamento,
            @RequestParam(required = false) Boolean reservavel
    ) {

        if (sortField.equals("tipoEquipamento")) sortField = "tipoEquipamento.id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var equipamentos = service.obter(pageable, id, tombamento, status, tipoEquipamento, reservavel);
        return ResponseEntity.ok(ApiResponseDTO.success(equipamentos));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<EquipamentoRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody EquipamentoAtualizarDTO data) {
        var atualizado = service.atualizar(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(atualizado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<Void>> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    /**
     * Obtém estatísticas de uso dos equipamentos em um período.
     * 
     * <p>Retorna estatísticas detalhadas sobre o uso dos equipamentos, incluindo:</p>
     * <ul>
     *   <li>Estatísticas por mês do período</li>
     *   <li>Mês com mais reservas (do período ou do ano se período for 1 mês)</li>
     *   <li>Usuários que mais reservaram no período</li>
     *   <li>Todos os usuários que reservaram no período</li>
     *   <li>Totais do período (reservas solicitadas e aprovadas)</li>
     * </ul>
     * 
     * <p>As estatísticas são agrupadas por equipamento e podem ser filtradas
     * por equipamentos específicos, tipo de equipamento, multiusuário ou espaço vinculado.</p>
     * 
     * @param mesInicial mês inicial para filtrar reservas (1-12), padrão = mês atual
     * @param anoInicial ano inicial para filtrar reservas, padrão = ano atual
     * @param mesFinal mês final para filtrar reservas (1-12), padrão = mês atual
     * @param anoFinal ano final para filtrar reservas, padrão = ano atual
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtrar por equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço vinculado para filtrar equipamentos (opcional)
     * @return estatísticas agrupadas por equipamento
     */
    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas de uso dos equipamentos",
               description = "Retorna estatísticas detalhadas sobre o uso dos equipamentos em um período, incluindo estatísticas por mês, " +
                           "mês com mais reservas e usuários que mais reservaram. Pode ser filtrado por período e equipamentos específicos.")
    public ResponseEntity<ApiResponseDTO<EstatisticasGeralEquipamentoDTO>> obterEstatisticas(
            @RequestParam(required = false) Integer mesInicial,
            @RequestParam(required = false) Integer anoInicial,
            @RequestParam(required = false) Integer mesFinal,
            @RequestParam(required = false) Integer anoFinal,
            @RequestParam(required = false) List<String> equipamentoIds,
            @RequestParam(required = false) String tipoEquipamentoId,
            @RequestParam(required = false) Boolean multiusuario,
            @RequestParam(required = false) String espacoId) {
        var estatisticas = service.obterEstatisticas(mesInicial, anoInicial, mesFinal, anoFinal, equipamentoIds, tipoEquipamentoId, multiusuario, espacoId);
        return ResponseEntity.ok(ApiResponseDTO.success(estatisticas));
    }

    /**
     * Gera PDF com estatísticas de uso dos equipamentos em um período.
     * 
     * <p>Retorna um arquivo PDF contendo as mesmas informações do endpoint de estatísticas,
     * formatado para impressão ou download.</p>
     * 
     * @param mesInicial mês inicial para filtrar reservas (1-12), padrão = mês atual
     * @param anoInicial ano inicial para filtrar reservas, padrão = ano atual
     * @param mesFinal mês final para filtrar reservas (1-12), padrão = mês atual
     * @param anoFinal ano final para filtrar reservas, padrão = ano atual
     * @param equipamentoIds lista de IDs de equipamentos para filtrar (opcional, padrão = todos os equipamentos)
     * @param tipoEquipamentoId ID do tipo de equipamento para filtrar (opcional)
     * @param multiusuario filtrar por equipamentos multiusuário (opcional)
     * @param espacoId ID do espaço vinculado para filtrar equipamentos (opcional)
     * @return arquivo PDF com as estatísticas
     */
    @GetMapping("/estatisticas/pdf")
    @Operation(summary = "Gerar PDF com estatísticas de uso dos equipamentos",
               description = "Gera um documento PDF contendo as estatísticas detalhadas de uso dos equipamentos em um período.")
    public ResponseEntity<byte[]> gerarPDFEstatisticas(
            @RequestParam(required = false) Integer mesInicial,
            @RequestParam(required = false) Integer anoInicial,
            @RequestParam(required = false) Integer mesFinal,
            @RequestParam(required = false) Integer anoFinal,
            @RequestParam(required = false) List<String> equipamentoIds,
            @RequestParam(required = false) String tipoEquipamentoId,
            @RequestParam(required = false) Boolean multiusuario,
            @RequestParam(required = false) String espacoId) throws java.io.IOException {
        byte[] pdfBytes = service.gerarPDFEstatisticas(mesInicial, anoInicial, mesFinal, anoFinal, equipamentoIds, tipoEquipamentoId, multiusuario, espacoId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "estatisticas-equipamentos.pdf");
        headers.setContentLength(pdfBytes.length);
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
