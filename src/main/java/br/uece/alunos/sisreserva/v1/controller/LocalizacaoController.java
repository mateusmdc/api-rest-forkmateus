package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.departamento.DepartamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.localizacao.LocalizacaoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.LocalizacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/localizacao")
@Tag(name = "Rotas de localização mapeadas no controller")
public class LocalizacaoController {
    @Autowired
    private LocalizacaoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<LocalizacaoRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nome
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var localizacoes = service.obter(pageable, id, nome);
        return ResponseEntity.ok(ApiResponseDTO.success(localizacoes));
    }
}
