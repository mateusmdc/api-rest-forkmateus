package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.comite.ComiteDTO;
import br.uece.alunos.sisreserva.v1.dto.comite.ComiteRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.ComiteService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/comite")
@Tag(name = "Rotas de comitê mapeadas no controller")
public class ComiteController {
    @Autowired
    private ComiteService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ComiteRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "tipo") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) Integer tipo
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var comites = service.obter(pageable, id, tipo);
        return ResponseEntity.ok(ApiResponseDTO.success(comites));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ComiteRetornoDTO>> criar(@RequestBody @Valid ComiteDTO data) {
        var comiteDTO = service.criar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(comiteDTO));
    }
}
