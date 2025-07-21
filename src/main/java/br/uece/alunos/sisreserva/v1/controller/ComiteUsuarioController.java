
package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioDTO;
import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.ComiteUsuarioService;
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
@RequestMapping("/comite/usuario")
@Tag(name = "Rotas de usuários do comitê mapeadas no controller")
public class ComiteUsuarioController {
    @Autowired
    private ComiteUsuarioService service;

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ComiteUsuarioRetornoDTO>>> obter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "comite") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String comiteId,
            @RequestParam(required = false) String usuarioId,
            @RequestParam(required = false) String departamentoId,
            @RequestParam(required = false) String portaria,
            @RequestParam(required = false) Boolean isTitular
    ) {
        var fieldToSort = switch (sortField) {
            case "comite" -> "comite.id";
            case "usuario" -> "usuario.id";
            case "departamento" -> "departamento.id";
            default -> sortField;
        };
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), fieldToSort));
        var retornoPaginado = service.obter(pageable, id, comiteId, usuarioId, departamentoId, portaria, isTitular);
        return ResponseEntity.ok(ApiResponseDTO.success(retornoPaginado));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<ComiteUsuarioRetornoDTO>> criar(
            @RequestBody @Valid ComiteUsuarioDTO data) {
        var comiteUsuarioCriado = service.criar(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(comiteUsuarioCriado));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<ComiteUsuarioRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody @Valid ComiteUsuarioAtualizarDTO data) {
        var atualizado = service.atualizar(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(atualizado));
    }
}
