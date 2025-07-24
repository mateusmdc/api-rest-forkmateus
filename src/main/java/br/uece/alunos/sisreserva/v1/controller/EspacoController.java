package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.comiteUsuario.ComiteUsuarioAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoAtualizarDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.espaco.EspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.EspacoService;
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
@RequestMapping("/espaco")
@Tag(name = "Rotas de espaço mapeadas no controller")
public class EspacoController {
    @Autowired
    private EspacoService espacoService;

    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponseDTO<EspacoRetornoDTO>> criarEspaco(@RequestBody @Valid EspacoDTO data) {
        var espacoRetornoDTO = espacoService.criarEspaco(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(espacoRetornoDTO));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDTO<EspacoRetornoDTO>> atualizar(
            @PathVariable String id,
            @RequestBody EspacoAtualizarDTO data) {
        var atualizado = espacoService.atualizar(id, data);
        return ResponseEntity.ok(ApiResponseDTO.success(atualizado));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<EspacoRetornoDTO>>> obterEspacosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "nome") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) String tipoEspaco,
            @RequestParam(required = false) String tipoAtividade,
            @RequestParam(required = false) String nome) {

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
        var espacosPaginados = espacoService.obterEspacos(pageable, id, departamento, localizacao, tipoEspaco, tipoAtividade, nome);
        return ResponseEntity.ok(ApiResponseDTO.success(espacosPaginados));
    }
}
