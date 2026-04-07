package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.AtualizarStatusSolicitacaoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.ExcecaoRecorrenciaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.OcorrenciaReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.HorariosOcupadosPorMesDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.RecorrenciaInfoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.SolicitacaoReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/solicitacao-reserva")
@Tag(name = "Rotas de solicitação de reserva mapeadas no controller")
public class SolicitacaoReservaController {

    @Autowired
    private SolicitacaoReservaService solicitacaoReservaService;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<SolicitacaoReservaRetornoDTO>> criarSolicitacaoReserva(
            @RequestBody @Valid SolicitacaoReservaDTO data) {
        var solicitacaoRetornoDTO = solicitacaoReservaService.criarSolicitacaoReserva(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(solicitacaoRetornoDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<SolicitacaoReservaRetornoDTO>>> obterSolicitacoesPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) String espacoId,
            @RequestParam(required = false) String equipamentoId,
            @RequestParam(required = false) String usuarioSolicitanteId,
            @RequestParam(required = false) Integer statusCodigo,
            @RequestParam(required = false) String projetoId,
            @Parameter(description = "Filtra reservas de equipamentos pertencentes ao espaço informado")
            @RequestParam(required = false) String espacoDoEquipamentoId,
            @Parameter(description = "Mês (1-12) para filtrar por período; séries recorrentes retornam ocorrências calculadas do mês")
            @RequestParam(required = false) Integer mes,
            @Parameter(description = "Ano para filtro de mês; usa o ano atual quando omitido")
            @RequestParam(required = false) Integer ano
    ) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var solicitacoesPaginadas = solicitacaoReservaService.obterSolicitacaoReserva(
                pageable, id, dataInicio, dataFim, espacoId, equipamentoId,
                usuarioSolicitanteId, statusCodigo, projetoId, espacoDoEquipamentoId, mes, ano
        );
        return ResponseEntity.ok(ApiResponseDTO.success(solicitacoesPaginadas));
    }

    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<ApiResponseDTO<SolicitacaoReservaRetornoDTO>> atualizarStatus(
            @PathVariable String id,
            @RequestBody @Valid AtualizarStatusSolicitacaoDTO data) {
        var solicitacaoAtualizada = solicitacaoReservaService.atualizarStatus(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(solicitacaoAtualizada));
    }

    @GetMapping("/horarios-ocupados")
    public ResponseEntity<ApiResponseDTO<HorariosOcupadosPorMesDTO>> obterHorariosOcupados(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String espacoId) {
        var horariosOcupados = solicitacaoReservaService.obterHorariosOcupadosPorMes(mes, ano, espacoId);
        return ResponseEntity.ok(ApiResponseDTO.success(horariosOcupados));
    }

    /**
     * Obtém informações completas sobre uma série recorrente.
     *
     * Retorna os dados da série (registro com {@code isSerie = true}) e todas as ocorrências
     * calculadas dinamicamente a partir da regra de recorrência, com eventuais exceções já aplicadas
     * (cancelamentos, reagendamentos ou confirmações individuais).
     *
     * @param id identificador da série recorrente ({@code isSerie = true})
     * @return informações completas da série e suas ocorrências calculadas
     */
    @GetMapping("/{id}/recorrencia")
    public ResponseEntity<ApiResponseDTO<RecorrenciaInfoDTO>> obterRecorrenciaInfo(@PathVariable String id) {
        var recorrenciaInfo = solicitacaoReservaService.obterRecorrenciaInfo(id);
        return ResponseEntity.ok(ApiResponseDTO.success(recorrenciaInfo));
    }

    /**
     * Cria ou atualiza uma exceção em uma ocorrência específica de uma série recorrente.
     *
     * Permite cancelar, reagendar ou confirmar uma ocorrência individual sem afetar
     * as demais ocorrências da série.
     *
     * @param id  ID da série recorrente
     * @param dto dados da exceção
     * @return dados da ocorrência atualizada
     */
    @PostMapping("/{id}/ocorrencias/excecao")
    @Transactional
    public ResponseEntity<ApiResponseDTO<OcorrenciaReservaDTO>> criarExcecaoOcorrencia(
            @PathVariable String id,
            @RequestBody @Valid ExcecaoRecorrenciaDTO dto) {
        var ocorrenciaAtualizada = solicitacaoReservaService.criarExcecaoOcorrencia(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.success(ocorrenciaAtualizada));
    }
}
