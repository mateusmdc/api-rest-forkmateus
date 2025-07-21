package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.equipamento.EquipamentoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.EquipamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/equipamento")
@Tag(name = "Rotas de equipamento mapeadas no controller")
public class EquipamentoController {
    @Autowired
    private EquipamentoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<EquipamentoRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "tombamento") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String tombamento,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tipoEquipamento
    ) {

        if (sortField.equals("tipoEquipamento")) sortField = "tipoEquipamento.id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var equipamentos = service.obter(pageable, id, tombamento, status, tipoEquipamento);
        return ResponseEntity.ok(ApiResponseDTO.success(equipamentos));
    }
}
