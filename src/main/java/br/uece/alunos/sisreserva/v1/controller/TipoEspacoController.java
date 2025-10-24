package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.tipoEspaco.TipoEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.TipoEspacoService;
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
@RequestMapping("/espaco/tipo")
@Tag(name = "Rotas de tipo de espaço mapeadas no controller")
public class TipoEspacoController {
    @Autowired
    private TipoEspacoService service;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<TipoEspacoRetornoDTO>> criar(@RequestBody @Valid TipoEspacoDTO data) {
        var tipoEspacoCriado = service.criar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(tipoEspacoCriado));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<TipoEspacoRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody TipoEspacoAtualizarDTO data) {
        var tipoEspacoAtualizado = service.atualizar(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(tipoEspacoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<Void>> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<TipoEspacoRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nome
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var tipoDeEspacos = service.obter(pageable, id, nome);
        return ResponseEntity.ok(ApiResponseDTO.success(tipoDeEspacos));
    }
}
