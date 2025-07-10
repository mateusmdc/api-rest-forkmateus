
package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoDTO;
import br.uece.alunos.sisreserva.v1.dto.gestorEspaco.GestorEspacoRetornoDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.ApiResponseDTO;
import br.uece.alunos.sisreserva.v1.service.GestorEspacoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        var gestorEspacoRetornoDTO = gestorEspacoService.cadastrarGestorEspaco(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success(gestorEspacoRetornoDTO));
    }
}
