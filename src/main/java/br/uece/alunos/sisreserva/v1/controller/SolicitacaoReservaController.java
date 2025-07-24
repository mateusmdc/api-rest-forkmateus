package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaDTO;
import br.uece.alunos.sisreserva.v1.dto.solicitacaoReserva.SolicitacaoReservaRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.SolicitacaoReservaService;
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
    public ResponseEntity<ApiResponseDTO<SolicitacaoReservaRetornoDTO>> criarSolicitacaoReserva(@RequestBody @Valid SolicitacaoReservaDTO data) {
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
        @RequestParam(required = false) String usuarioSolicitanteId,
        @RequestParam(required = false) Integer statusCodigo,
        @RequestParam(required = false) String projetoId
    ) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var solicitacoesPaginadas = solicitacaoReservaService.obterSolicitacaoReserva(
            pageable, id, dataInicio, dataFim, espacoId, usuarioSolicitanteId, statusCodigo, projetoId
        );
        return ResponseEntity.ok(ApiResponseDTO.success(solicitacoesPaginadas));
    }
}
