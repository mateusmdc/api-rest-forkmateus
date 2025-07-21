
package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.GestorEspacoService;
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

@RestController
@RequestMapping("/espaco/gestor")
@Tag(name = "Rotas de gestor espaço mapeadas no controller")
public class GestorEspacoController {
    @Autowired
    private GestorEspacoService gestorEspacoService;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<GestorEspacoRetornoDTO>> cadastrarGestorEspaco(
            @RequestBody @Valid GestorEspacoDTO data) {
        var gestorEspacoRetornoDTO = gestorEspacoService.cadastrarOuReativarGestorEspaco(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(gestorEspacoRetornoDTO));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<GestorEspacoRetornoDTO>> inativarGestorEspaco(@PathVariable String id) {
        gestorEspacoService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<GestorEspacoRetornoDTO>>> obterGestorEspacoPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String espaco,
            @RequestParam(required = false) String gestor,
            @RequestParam(defaultValue = "false") boolean todos) {
        var fieldToSort = switch (sortField) {
            case "gestor" -> "usuarioGestor.id";
            case "espaco" -> "espaco.id";
            default -> sortField;
        };
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), fieldToSort));
        var gestoresPaginados = gestorEspacoService.obter(pageable, id, espaco, gestor, todos);
        return ResponseEntity.ok(ApiResponseDTO.success(gestoresPaginados));
    }
}
