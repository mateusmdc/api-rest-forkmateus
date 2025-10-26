package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.complexoEspacos.ComplexoEspacosVincularEspacosDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.ComplexoEspacosService;
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

import java.util.List;

@RestController
@RequestMapping("/complexo-espacos")
@Tag(name = "Rotas de complexo de espaços mapeadas no controller")
public class ComplexoEspacosController {
    @Autowired
    private ComplexoEspacosService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<ComplexoEspacosRetornoDTO>> criar(@RequestBody @Valid ComplexoEspacosDTO data) {
        var complexoCriado = service.criar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(complexoCriado));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<ComplexoEspacosRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody ComplexoEspacosAtualizarDTO data) {
        var complexoAtualizado = service.atualizar(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(complexoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<Void>> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ComplexoEspacosRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nome
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var complexos = service.obter(pageable, id, nome);
        return ResponseEntity.ok(ApiResponseDTO.success(complexos));
    }

    @PostMapping("/{id}/espacos")
    @Transactional
    public ResponseEntity<ApiResponseDTO<ComplexoEspacosRetornoDTO>> atribuirEspacos(
            @PathVariable String id,
            @RequestBody @Valid ComplexoEspacosVincularEspacosDTO data) {
        var complexoAtualizado = service.atribuirEspacos(id, data.espacoIds());
        return ResponseEntity.ok(ApiResponseDTO.success(complexoAtualizado));
    }

    @DeleteMapping("/{id}/espacos")
    @Transactional
    public ResponseEntity<ApiResponseDTO<ComplexoEspacosRetornoDTO>> desatribuirEspacos(
            @PathVariable String id,
            @RequestBody @Valid ComplexoEspacosVincularEspacosDTO data) {
        var complexoAtualizado = service.desatribuirEspacos(id, data.espacoIds());
        return ResponseEntity.ok(ApiResponseDTO.success(complexoAtualizado));
    }

    @GetMapping("/{id}/espacos")
    public ResponseEntity<ApiResponseDTO<List<EspacoRetornoDTO>>> listarEspacos(@PathVariable String id) {
        var espacos = service.listarEspacos(id);
        return ResponseEntity.ok(ApiResponseDTO.success(espacos));
    }
}
