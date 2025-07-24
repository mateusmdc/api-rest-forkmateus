package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEquipamento.TipoEquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEquipamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipamento/tipo")
@Tag(name = "Rotas de tipo de equipamento mapeadas no controller")
public class TipoEquipamentoController {
    @Autowired
    private TipoEquipamentoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<TipoEquipamentoRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nome
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var tiposDeEquipamento = service.obter(pageable, id, nome);
        return ResponseEntity.ok(ApiResponseDTO.success(tiposDeEquipamento));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TipoEquipamentoRetornoDTO>> criar (@RequestBody @Valid TipoEquipamentoDTO data) {
        var tipoEquipamentoDTO = service.criar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(tipoEquipamentoDTO));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<TipoEquipamentoRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody TipoEquipamentoAtualizarDTO data) {
        var atualizado = service.atualizar(data, id);
        return ResponseEntity.ok(ApiResponseDTO.success(atualizado));
    }
}
